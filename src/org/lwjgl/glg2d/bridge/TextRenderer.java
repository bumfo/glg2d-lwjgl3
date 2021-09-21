package org.lwjgl.glg2d.bridge;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextRenderer {
  private final Font font;
  private final boolean antialiased;
  private final boolean useFractionalMetrics;
  private final GlyphProducer mGlyphProducer;

  private Graphics2D cachedGraphics;
  private FontRenderContext cachedFontRenderContext;

  private boolean smoothing;
  private Color color;

  public TextRenderer(final Font font, final boolean antialiased,
                      final boolean useFractionalMetrics) {
    this.font = font;
    this.antialiased = antialiased;
    this.useFractionalMetrics = useFractionalMetrics;

    mGlyphProducer = new GlyphProducer(font.getNumGlyphs());
  }

  public void setSmoothing(boolean smoothing) {
    this.smoothing = smoothing;
  }

  public boolean getSmoothing() {
    return smoothing;
  }

  public void dispose() {
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public Color getColor() {
    return color;
  }

  public void begin3DRendering() {
  }

  public void end3DRendering() {
  }

  /**
   * Returns a FontRenderContext which can be used for external
   * text-related size computations. This object should be considered
   * transient and may become invalidated between beginRendering / endRendering pairs.
   */
  public FontRenderContext getFontRenderContext() {
    if (cachedFontRenderContext == null) {
      cachedFontRenderContext = getGraphics2D().getFontRenderContext();
    }

    return cachedFontRenderContext;
  }

  private Graphics2D getGraphics2D() {
    // final TextureRenderer renderer = getBackingStore();

    if (cachedGraphics == null) {
      // cachedGraphics = renderer.createGraphics();

      cachedGraphics = (Graphics2D) new Frame().getGraphics();

      // Set up composite, font and rendering hints
      cachedGraphics.setComposite(AlphaComposite.Src);
      cachedGraphics.setColor(Color.WHITE);
      cachedGraphics.setFont(font);
      cachedGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
          (antialiased ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON
              : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF));
      cachedGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
          (useFractionalMetrics
              ? RenderingHints.VALUE_FRACTIONALMETRICS_ON
              : RenderingHints.VALUE_FRACTIONALMETRICS_OFF));
    }

    return cachedGraphics;
  }

  public void draw3D(final String str, final float x, final float y, final float z, final float scaleFactor) {
    internal_draw3D(str, x, y, z, scaleFactor);
  }

  public void draw3D(final CharSequence str, final float x, final float y, final float z, final float scaleFactor) {
    internal_draw3D(str, x, y, z, scaleFactor);
  }

  private void internal_draw3D(final CharSequence str, float x, final float y, final float z,
                               final float scaleFactor) {
    for (final Glyph glyph : mGlyphProducer.getGlyphs(str)) {
      final float advance = glyph.draw3D(x, y, z, scaleFactor);
      x += advance * scaleFactor;
    }
  }

  // A temporary to prevent excessive garbage creation
  private final char[] singleUnicode = new char[1];

  private static final boolean DISABLE_GLYPH_CACHE = false;

  class GlyphProducer {
    static final int undefined = -2;
    List<Glyph> glyphsOutput = new ArrayList<Glyph>();
    HashMap<String, GlyphVector> fullGlyphVectorCache = new HashMap<String, GlyphVector>();
    HashMap<Character, GlyphMetrics> glyphMetricsCache = new HashMap<Character, GlyphMetrics>();

    // The mapping from unicode character to font-specific glyph ID
    int[] unicodes2Glyphs;
    // The mapping from glyph ID to Glyph
    Glyph[] glyphCache;
    CharSequenceIterator iter = new CharSequenceIterator();

    GlyphProducer(final int fontLengthInGlyphs) {
      unicodes2Glyphs = new int[512];
      glyphCache = new Glyph[fontLengthInGlyphs];
      clearAllCacheEntries();
    }


    public List<Glyph> getGlyphs(final CharSequence inString) {
      glyphsOutput.clear();
      GlyphVector fullRunGlyphVector;
      fullRunGlyphVector = fullGlyphVectorCache.get(inString.toString());
      if (fullRunGlyphVector == null) {
        iter.initFromCharSequence(inString);
        fullRunGlyphVector = font.createGlyphVector(getFontRenderContext(), iter);
        fullGlyphVectorCache.put(inString.toString(), fullRunGlyphVector);
      }
      final boolean complex = (fullRunGlyphVector.getLayoutFlags() != 0);
      if (complex || DISABLE_GLYPH_CACHE) {
        // Punt to the robust version of the renderer
        glyphsOutput.add(new Glyph(inString.toString(), false));
        return glyphsOutput;
      }

      final int lengthInGlyphs = fullRunGlyphVector.getNumGlyphs();
      int i = 0;
      while (i < lengthInGlyphs) {
        final Character letter = CharacterCache.valueOf(inString.charAt(i));
        GlyphMetrics metrics = glyphMetricsCache.get(letter);
        if (metrics == null) {
          metrics = fullRunGlyphVector.getGlyphMetrics(i);
          glyphMetricsCache.put(letter, metrics);
        }
        final Glyph glyph = getGlyph(inString, metrics, i);
        if (glyph != null) {
          glyphsOutput.add(glyph);
          i++;
        } else {
          // Assemble a run of characters that don't fit in
          // the cache
          final StringBuilder buf = new StringBuilder();
          while (i < lengthInGlyphs &&
              getGlyph(inString, fullRunGlyphVector.getGlyphMetrics(i), i) == null) {
            buf.append(inString.charAt(i++));
          }
          glyphsOutput.add(new Glyph(buf.toString(),
              // Any more glyphs after this run?
              i < lengthInGlyphs));
        }
      }
      return glyphsOutput;
    }

    // Returns a glyph object for this single glyph. Returns null
    // if the unicode or glyph ID would be out of bounds of the
    // glyph cache.
    private Glyph getGlyph(final CharSequence inString,
                           final GlyphMetrics glyphMetrics,
                           final int index) {
      final char unicodeID = inString.charAt(index);

      if (unicodeID >= unicodes2Glyphs.length) {
        return null;
      }

      final int glyphID = unicodes2Glyphs[unicodeID];
      if (glyphID != undefined) {
        return glyphCache[glyphID];
      }

      // Must fabricate the glyph
      singleUnicode[0] = unicodeID;
      final GlyphVector gv = font.createGlyphVector(getFontRenderContext(), singleUnicode);
      return getGlyph(unicodeID, gv, glyphMetrics);
    }

    // It's unclear whether this variant might produce less
    // optimal results than if we can see the entire GlyphVector
    // for the incoming string
    private Glyph getGlyph(final int unicodeID) {
      if (unicodeID >= unicodes2Glyphs.length) {
        return null;
      }

      final int glyphID = unicodes2Glyphs[unicodeID];
      if (glyphID != undefined) {
        return glyphCache[glyphID];
      }
      singleUnicode[0] = (char) unicodeID;
      final GlyphVector gv = font.createGlyphVector(getFontRenderContext(), singleUnicode);
      return getGlyph(unicodeID, gv, gv.getGlyphMetrics(0));
    }

    private Glyph getGlyph(final int unicodeID,
                           final GlyphVector singleUnicodeGlyphVector,
                           final GlyphMetrics metrics) {
      final int glyphCode = singleUnicodeGlyphVector.getGlyphCode(0);
      // Have seen huge glyph codes (65536) coming out of some fonts in some Unicode situations
      if (glyphCode >= glyphCache.length) {
        return null;
      }
      final Glyph glyph = new Glyph(unicodeID,
          glyphCode,
          metrics.getAdvance(),
          singleUnicodeGlyphVector,
          this);
      register(glyph);
      return glyph;
    }

    public void clearCacheEntry(final int unicodeID) {
      final int glyphID = unicodes2Glyphs[unicodeID];
      if (glyphID != undefined) {
        final Glyph glyph = glyphCache[glyphID];
        if (glyph != null) {
          glyph.clear();
        }
        glyphCache[glyphID] = null;
      }
      unicodes2Glyphs[unicodeID] = undefined;
    }

    public void clearAllCacheEntries() {
      for (int i = 0; i < unicodes2Glyphs.length; i++) {
        clearCacheEntry(i);
      }
    }

    public void register(final Glyph glyph) {
      unicodes2Glyphs[glyph.getUnicodeID()] = glyph.getGlyphCode();
      glyphCache[glyph.getGlyphCode()] = glyph;
    }
  }

  class Glyph {
    // The rectangle of this glyph on the backing store, or null
    // if it has been cleared due to space pressure
    private Object glyphRectForTextureMapping; // Rect

    // If this Glyph represents an individual unicode glyph, this
    // is its unicode ID. If it represents a String, this is -1.
    private int unicodeID;
    // If the above field isn't -1, then these fields are used.
    // The glyph code in the font
    private int glyphCode;
    // The GlyphProducer which created us
    private GlyphProducer producer;
    // The advance of this glyph
    private float advance;
    // The GlyphVector for this single character; this is passed
    // in during construction but cleared during the upload
    // process
    private GlyphVector singleUnicodeGlyphVector;
    // If this Glyph represents a String, this is the sequence of
    // characters
    private String str;
    // Whether we need a valid advance when rendering this string
    // (i.e., whether it has other single glyphs coming after it)
    private boolean needAdvance;

    // Creates a Glyph representing an individual Unicode character
    public Glyph(final int unicodeID,
                 final int glyphCode,
                 final float advance,
                 final GlyphVector singleUnicodeGlyphVector,
                 final GlyphProducer producer) {
      this.unicodeID = unicodeID;
      this.glyphCode = glyphCode;
      this.advance = advance;
      this.singleUnicodeGlyphVector = singleUnicodeGlyphVector;
      this.producer = producer;
    }

    // Creates a Glyph representing a sequence of characters, with
    // an indication of whether additional single glyphs are being
    // rendered after it
    public Glyph(final String str, final boolean needAdvance) {
      this.str = str;
      this.needAdvance = needAdvance;
    }

    /** Notifies this glyph that it's been cleared out of the cache */
    public void clear() {
      glyphRectForTextureMapping = null;
    }

    /** Returns this glyph's unicode ID */
    public int getUnicodeID() {
      return unicodeID;
    }

    /** Returns this glyph's (font-specific) glyph code */
    public int getGlyphCode() {
      return glyphCode;
    }

    /** Returns the advance for this glyph */
    public float getAdvance() {
      return advance;
    }

    public float draw3D(final float inX, final float inY, final float z, final float scaleFactor) {
      return 0f;
    }
  }

  private static final class CharacterCache {
    private CharacterCache() {
    }

    static final Character cache[] = new Character[127 + 1];

    static {
      for (int i = 0; i < cache.length; i++) {
        cache[i] = Character.valueOf((char) i);
      }
    }

    public static Character valueOf(final char c) {
      if (c <= 127) { // must cache
        return CharacterCache.cache[c];
      }
      return Character.valueOf(c);
    }
  }

  private static final class CharSequenceIterator implements CharacterIterator {
    CharSequence mSequence;
    int mLength;
    int mCurrentIndex;

    CharSequenceIterator() {
    }

    CharSequenceIterator(final CharSequence sequence) {
      initFromCharSequence(sequence);
    }

    public void initFromCharSequence(final CharSequence sequence) {
      mSequence = sequence;
      mLength = mSequence.length();
      mCurrentIndex = 0;
    }

    @Override
    public char last() {
      mCurrentIndex = Math.max(0, mLength - 1);

      return current();
    }

    @Override
    public char current() {
      if ((mLength == 0) || (mCurrentIndex >= mLength)) {
        return CharacterIterator.DONE;
      }

      return mSequence.charAt(mCurrentIndex);
    }

    @Override
    public char next() {
      mCurrentIndex++;

      return current();
    }

    @Override
    public char previous() {
      mCurrentIndex = Math.max(mCurrentIndex - 1, 0);

      return current();
    }

    @Override
    public char setIndex(final int position) {
      mCurrentIndex = position;

      return current();
    }

    @Override
    public int getBeginIndex() {
      return 0;
    }

    @Override
    public int getEndIndex() {
      return mLength;
    }

    @Override
    public int getIndex() {
      return mCurrentIndex;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public CharSequenceIterator clone() {
      final CharSequenceIterator iter = new CharSequenceIterator(mSequence);
      iter.mCurrentIndex = mCurrentIndex;

      return iter;
    }

    @Override
    public char first() {
      if (mLength == 0) {
        return CharacterIterator.DONE;
      }

      mCurrentIndex = 0;

      return current();
    }
  }
}
