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
package org.acre.visualizer.ui.pqleditor;

import org.acre.model.metamodel.FactMetaModel;

import javax.swing.text.*;
import java.awt.Color;

/**
 * @author Syed Ali
 */
public class CodeDocument extends DefaultStyledDocument {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -382262972263008414L;

    private String word = "";

    private final static SimpleAttributeSet SYS_VAR_ATTR = new SimpleAttributeSet();

    private final static SimpleAttributeSet KEYWORD_ATTR = new SimpleAttributeSet();

    private final static SimpleAttributeSet NORMAL_ATTR = new SimpleAttributeSet();

    private final static SimpleAttributeSet STRING_ATTR = new SimpleAttributeSet();

    private final static SimpleAttributeSet NUMBER_ATTR = new SimpleAttributeSet();

    private final static SimpleAttributeSet COMMENTS_ATTR = new SimpleAttributeSet();

    private int currentPos = 0;

    private int wordStart;

    public static int STRING_MODE = 10;

    public static int TEXT_MODE = 11;

    public static int NUMBER_MODE = 12;

    public static int COMMENT_MODE = 13;
    public static int LINE_COMMENT_MODE = 14;

    private int mode = TEXT_MODE;

    private boolean debug = false;

    static {
        initAttributes();
    }

    private final static void initAttributes() {
        //set the attributes
        StyleConstants.setFontFamily(NORMAL_ATTR, "Courier New");
        StyleConstants.setFontFamily(KEYWORD_ATTR, "Courier New");
        StyleConstants.setFontFamily(SYS_VAR_ATTR, "Courier New");
        StyleConstants.setFontFamily(STRING_ATTR, "Courier New");
        StyleConstants.setFontFamily(NUMBER_ATTR, "Courier New");
        StyleConstants.setFontFamily(COMMENTS_ATTR, "Courier New");
        StyleConstants.setBold(KEYWORD_ATTR, true);
        StyleConstants.setItalic(COMMENTS_ATTR, true);
        StyleConstants.setForeground(KEYWORD_ATTR, new Color(0,0,128)); // DARK PURPLE
        StyleConstants.setForeground(SYS_VAR_ATTR, new Color(102, 14, 122));
        StyleConstants.setForeground(STRING_ATTR, new Color(0,128,0)); // DARK GREEN
        StyleConstants.setForeground(NUMBER_ATTR, new Color(0,0,255)); // BLUE
        StyleConstants.setForeground(COMMENTS_ATTR, new Color(128, 128, 128)); // LIGHT GRAY
    }

    public CodeDocument() {
    }

    private void checkForString() {
        int offs = this.currentPos;
        Element element = this.getParagraphElement(offs);
        String elementText = "";
        try {
            //this gets our chuck of current text for the element we're on
            elementText = this.getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset());
        } catch (Exception ex) {
            //whoops!
            //System.out.println("no text");
        }
        int strLen = elementText.length();
        if (strLen == 0) { return; }
        int i = 0;

        if (element.getStartOffset() > 0) {
            //translates backward if neccessary
            offs = offs - element.getStartOffset();
        }
        int quoteCount = 0;
        if ((offs >= 0) && (offs <= strLen - 1)) {
            i = offs;
            while (i > 0) {
                //the while loop walks back until we hit a delimiter
                char charAt = elementText.charAt(i);
                if ((charAt == '"')) {
                    quoteCount++;
                }
                i--;
            }
            int rem = quoteCount % 2;
            //System.out.println(rem);
            mode = (rem == 0) ? TEXT_MODE : STRING_MODE;
            //if (debug) System.err.println((rem == 0) ? "TEXT_MODE 1" : "STRING_MODE");
        }
    }

    private void checkForSyntaxHighlight() {
        int offs = this.currentPos;
        Element element = this.getParagraphElement(offs);
        String elementText = "";
        try {
            //this gets our chuck of current text for the element were on
            elementText = this.getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset());
        } catch (Exception ex) {
            //whoops!
            System.out.println("no text");
        }
        int strLen = elementText.length();
        if (strLen == 0) { return; }
        int i = 0;

        if (element.getStartOffset() > 0) {
            //translates backward if neccessary
            offs = offs - element.getStartOffset();
        }
        if ((offs >= 0) && (offs <= strLen - 1)) {
            i = offs;
            while (i > 0) {
                //the while loop walks back until we hit a delimiter
                i--;
                char charAt = elementText.charAt(i);
                if ((charAt == ' ') | (i == 0) | (charAt == '(')
                        | (charAt == ')') | (charAt == '{') | (charAt == '}')) { 
                    //if i == 0 then we're at the begininng
                    if (i != 0) {
                        i++;
                    }
                    word = elementText.substring(i, offs);//skip the period

                    String s = word.trim().toLowerCase();
                    //this is what actually checks for a matching keyword
                    if (FactMetaModel.getInstance().isPQLKeyword(s)) {
                        changeStyle(KEYWORD_ATTR);
                    } else if (FactMetaModel.getInstance().isMetaType(s)) {
                        changeStyle(SYS_VAR_ATTR);
                    }
                    break;
                }
            }
        }
    }

    private void checkForNumber() {
        int offs = this.currentPos;
        Element element = this.getParagraphElement(offs);
        String elementText = "";
        try {
            //this gets our chuck of current text for the element we're on
            elementText = this.getText(element.getStartOffset(), element
                    .getEndOffset()
                    - element.getStartOffset());
        } catch (Exception ex) {
            //whoops!
            System.out.println("no text");
        }
        int strLen = elementText.length();
        if (strLen == 0) { return; }
        int i = 0;

        if (element.getStartOffset() > 0) {
            //translates backward if neccessary
            offs = offs - element.getStartOffset();
        }
        mode = TEXT_MODE;
        if (debug) System.err.println("TEXT_MODE 2");
        if ((offs >= 0) && (offs <= strLen - 1)) {
            i = offs;
            while (i > 0) {
                //the while loop walks back until we hit a delimiter
                char charAt = elementText.charAt(i);
                if ((charAt == ' ') | (i == 0) | (charAt == '(')
                        | (charAt == ')') | (charAt == '{') | (charAt == '}') /* | */) { 
                    //if i == 0 then we're at the begininng
                    if (i != 0) {
                        i++;
                    }
                    mode = NUMBER_MODE;
                    if (debug) System.err.println("NUMBER_MODE");
                    break;
                } else if (!(charAt >= '0' & charAt <= '9' | charAt == '.'
                        | charAt == '+' | charAt == '-' | charAt == '/'
                        | charAt == '*' | charAt == '%' | charAt == '=')) {
                    mode = TEXT_MODE;
                    if (debug) System.err.println("TEXT_MODE 3");
                    break;
                }
                i--;
            }
        }
    }

    private void checkForComment() {
        int offs = this.currentPos;
        Element element = this.getParagraphElement(offs);
        String elementText = "";
        try {
            //this gets our chuck of current text for the element we're on
            elementText = this.getText(element.getStartOffset(), element
                    .getEndOffset()
                    - element.getStartOffset());
        } catch (Exception ex) {
            //whoops!
            System.out.println("no text");
        }
        int strLen = elementText.length();
        if (strLen == 0) { return; }
        int i = 0;

        if (element.getStartOffset() > 0) {
            //translates backward if neccessary
            offs = offs - element.getStartOffset();
        }
        if ((offs >= 1) && (offs <= strLen - 1)) {
            i = offs;
            char commentStartChar1 = elementText.charAt(i - 1);
            char commentStartChar2 = elementText.charAt(i);
            if (commentStartChar1 == '/' && commentStartChar2 == '*') {
                mode = COMMENT_MODE;
                if (debug) System.err.println("COMMENT_MODE 1");
                changeStyle("/*", currentPos - 1, COMMENTS_ATTR);
            } else if (commentStartChar1 == '*' && commentStartChar2 == '/') {
                mode = TEXT_MODE;
                if (debug) System.err.println("TEXT_MODE 4");
                changeStyle("*/", currentPos - 1, COMMENTS_ATTR);
            } else if (commentStartChar1 == '/' && commentStartChar2 == '/') {
                mode = LINE_COMMENT_MODE;
                if (debug) System.err.println("COMMENT_MODE 2");
                changeStyle("//", currentPos - 1, COMMENTS_ATTR);
//            } else if (mode == LINE_COMMENT_MODE && commentStartChar2 == '\n') {
//                mode = TEXT_MODE;
//                changeStyle("\n", currentPos - 1, COMMENTS_ATTR);
            }
        }
    }

    private void changeStyle(SimpleAttributeSet style) {
        try {
            //remove the old word and formatting
            this.remove(currentPos - word.length(), word.length());
            /*
             * replace it with the same word, but new formatting we MUST call
             * the super class insertString method here, otherwise we would end
             * up in an infinite loop !!!!!
             */
            super.insertString(currentPos - word.length(), word, style);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void changeStyle(String str, int pos, SimpleAttributeSet style) {
        try {
            //remove the old word and formatting
            this.remove(pos, str.length());
            super.insertString(pos, str, style);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processChar(String str) {
        char strChar = str.charAt(0);
        if (mode != COMMENT_MODE && mode != LINE_COMMENT_MODE) {
            mode = TEXT_MODE;
            if (debug) System.err.println("TEXT_MODE 5");
        }
        switch (strChar) {
        case ('{'):
        case ('}'):
        case (' '):
        case ('\n'):
        case ('('):
        case (')'):
        case (';'):
        case ('.'): {
            if (mode == TEXT_MODE) {
                checkForSyntaxHighlight();
            } else if ((mode == STRING_MODE || mode == LINE_COMMENT_MODE )
                    && strChar == '\n') {
                mode = TEXT_MODE;
                if (debug) System.err.println("TEXT_MODE 6");
            }
        }
            break;
        case ('"'): {
            if (mode == TEXT_MODE) {
                changeStyle(str, currentPos, STRING_ATTR);
                this.checkForString();
            }
        }
            break;
        case ('0'):
        case ('1'):
        case ('2'):
        case ('3'):
        case ('4'):
        case ('5'):
        case ('6'):
        case ('7'):
        case ('8'):
        case ('9'): {
            if (mode == TEXT_MODE) {
                checkForNumber();
            }
        }
            break;
        case ('*'):
        case ('/'): {
//            if (mode == TEXT_MODE) {
                checkForComment();
//            }
        }
            break;
        }
        if (mode == TEXT_MODE) {
            this.checkForString();
        }
        if (mode == STRING_MODE) {
            changeStyle(str, currentPos, STRING_ATTR);
        } else if (mode == NUMBER_MODE) {
            changeStyle(str, currentPos, NUMBER_ATTR);
        } else if (mode == COMMENT_MODE || mode == LINE_COMMENT_MODE) {
            changeStyle(str, currentPos, COMMENTS_ATTR);
        }

    }

    private void processChar(char strChar) {
        char[] chrstr = new char[1];
        chrstr[0] = strChar;
        String str = new String(chrstr);
        processChar(str);
    }

    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        super.insertString(offs, str, NORMAL_ATTR);

        int strLen = str.length();
        int endpos = offs + strLen;
        int strpos;
        for (int i = offs; i < endpos; i++) {
            currentPos = i;
            strpos = i - offs;
            processChar(str.charAt(strpos));
        }
        currentPos = offs;
    }
    
}
