/**
 *   Copyright 2004-2005 Sun Microsystems, Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.acre.visualizer.ui.codehighlight;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.io.IOException;
import java.io.Reader;

/**
 * Display text with syntax highlighting.
 */
public class SyntaxHighlighter extends JTextPane implements DocumentListener, TokenTypes {
    private StyledDocument doc;
    private Scanner scanner;
    private int height, width;

    // An array of styles, indexed by token type.  Default styles are set up,
    // which can be used for any languages.
    Style[] styles;

    Segment text = new Segment();
    int firstRehighlightToken;
    int smallAmount = 800;

    /**
     * Create a graphics component which displays text with syntax highlighting.
     * Provide a width and height, in characters, and a language scanner.
     */
    public SyntaxHighlighter(int height, int width, Scanner scanner) {
        super(new DefaultStyledDocument());
        doc = (StyledDocument) getDocument();
        this.height = height;
        this.width = width;
        this.scanner = scanner;
        doc.addDocumentListener(this);
        Font font = new Font("Courier New", Font.PLAIN, getFont().getSize());
        changeFont(font);
        initStyles();
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
    public Scanner getScanner() {
          return scanner;
    }

    public void initStyles() {
        styles = new Style[typeNames.length];
        changeStyle(UNRECOGNIZED, Color.red);
        changeStyle(WHITESPACE, Color.black);
        changeStyle(WORD, Color.black);
        changeStyle(NUMBER, Color.blue);
        changeStyle(COMMENT, Color.GRAY, Font.BOLD | Font.ITALIC);
        changeStyle(START_COMMENT, Color.GRAY, Font.BOLD);
        changeStyle(MID_COMMENT, Color.GRAY, Font.BOLD | Font.ITALIC);
        changeStyle(END_COMMENT, Color.GRAY, Font.BOLD);
        changeStyle(TAG, Color.blue, Font.BOLD);
        changeStyle(END_TAG, Color.blue, Font.BOLD);
        changeStyle(KEYWORD, Color.blue.darker(), Font.BOLD);
        changeStyle(KEYWORD2, Color.blue.darker(), Font.BOLD);
        changeStyle(IDENTIFIER, Color.black);
        changeStyle(LITERAL, Color.blue);
        changeStyle(STRING, Color.green.darker().darker(), Font.BOLD);
        changeStyle(CHARACTER, Color.green.darker().darker(), Font.BOLD);
        changeStyle(OPERATOR, Color.orange.darker().darker().darker(), Font.BOLD);
        changeStyle(BRACKET, Color.orange.darker().darker().darker(), Font.BOLD);
        changeStyle(SEPARATOR, Color.orange.darker().darker().darker(), Font.BOLD);
        changeStyle(PUNCTUATION, Color.orange.darker().darker().darker(), Font.BOLD);
        changeStyle(URL, Color.blue.darker());

        for (int i = 0; i < styles.length; i++) {
            if (styles[i] == null) {
                styles[i] = styles[WHITESPACE];
            }
        }
    }

    /**
     * Change the component's font, and change the size of the component to
     * match.
     */
    public void changeFont(Font font) {
        int borderOfJTextPane = 3;
        setFont(font);
        FontMetrics metrics = getFontMetrics(font);
        int paneWidth = width * metrics.charWidth('m') + 2 * borderOfJTextPane;
        int paneHeight = height * metrics.getHeight() + 2 * borderOfJTextPane;
        Dimension size = new Dimension(paneWidth, paneHeight);
        setMinimumSize(size);
        setPreferredSize(size);
        invalidate();
    }

    /**
     * Read new text into the component from a <code>Reader</code>.  Overrides
     * <code>read</code> in <code>JTextComponent</code> in order to highlight
     * the new text.
     */
    public void read(Reader in, Object desc) throws IOException {
        int oldLength = getDocument().getLength();
        doc.removeDocumentListener(this);
        super.read(in, desc);
        doc = (StyledDocument) getDocument();
        doc.addDocumentListener(this);
        int newLength = getDocument().getLength();
        firstRehighlightToken = scanner.change(0, oldLength, newLength);
        repaint();
    }

    /**
     * Change the style of a particular type of token.
     */
    public void changeStyle(int type, Color color) {
        Style style = addStyle(typeNames[type], null);
        StyleConstants.setForeground(style, color);
        styles[type] = style;
    }

    /**
     * Change the style of a particular type of token, including adding bold or
     * italic using a third argument of <code>Font.BOLD</code> or
     * <code>Font.ITALIC</code> or the bitwise union
     * <code>Font.BOLD|Font.ITALIC</code>.
     */
    public void changeStyle(int type, Color color, int fontStyle) {
        Style style = addStyle(typeNames[type], null);
        StyleConstants.setForeground(style, color);
        if ((fontStyle & Font.BOLD) != 0) {
            StyleConstants.setBold(style, true);
        }
        if ((fontStyle & Font.ITALIC) != 0) {
            StyleConstants.setItalic(style, true);
        }
        styles[type] = style;
    }

    /**
     * Responds to the
     * underlying document changes by re-highlighting.</font>
     */
    public void insertUpdate(DocumentEvent e) {
        int offset = e.getOffset();
        int length = e.getLength();
        firstRehighlightToken = scanner.change(offset, 0, length);
        repaint();
    }

    /**
     * Responds to the
     * underlying document changes by re-highlighting.</font>
     */
    public void removeUpdate(DocumentEvent e) {
        int offset = e.getOffset();
        int length = e.getLength();
        firstRehighlightToken = scanner.change(offset, length, 0);
        repaint();
    }

    /**
     * Responds to the
     * underlying document changes by re-highlighting.</font>
     */
    public void changedUpdate(DocumentEvent e) {
        // Nothing to do
    }

    /**
     * Carries out a small
     * amount of re-highlighting for each call to <code>repaint</code>.</font>
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int offset = scanner.position();
        if (offset < 0) {
            return;
        }

        int tokensToRedo = 0;
        int amount = smallAmount;
        while (tokensToRedo == 0 && offset >= 0) {
            int length = doc.getLength() - offset;
            if (length > amount) {
                length = amount;
            }
            try {
                doc.getText(offset, length, text);
            } catch (BadLocationException e) {
                return;
            }
            tokensToRedo = scanner.scan(text.array, text.offset, text.count);
            offset = scanner.position();
            amount = 2 * amount;
        }
        for (int i = 0; i < tokensToRedo; i++) {
            Token t = scanner.getToken(firstRehighlightToken + i);
            int length = t.symbol.name.length();
            int type = t.symbol.type;
            if (type < 0) {
                type = UNRECOGNIZED;
            }
            doc.setCharacterAttributes(t.position, length, styles[type], false);
        }
        firstRehighlightToken += tokensToRedo;
        if (offset >= 0) {
            repaint(2);
        }
    }
}
