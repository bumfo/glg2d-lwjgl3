/*
 * Copyright 2015 Brandon Borkholder
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jogamp.glg2d.impl;

import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GLG2DNotImplemented {
  private static final Logger LOGGER = Logger.getLogger(GLG2DNotImplemented.class.getName());

  private static final Set<String> tags = new TreeSet<String>();

  public static void notImplemented(String tag) {
    if (tags.add(tag)) {
      LOGGER.log(Level.WARNING, tag + " has not been implemented yet ...");
    }
  }
}
