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

import java.util.HashMap;

/**
 * Simple Scanner                                                        l
 */
public class Scanner implements TokenTypes {
    private Token[] tokens;
    private int gap, endgap, textLength;
    private boolean scanning;
    private int position;

    protected HashMap symbolTable;
    protected char[] buffer;
    protected int start;
    protected int end;
    protected int state = WHITESPACE;

    protected int read() {
        char c = buffer[start];
        int type;
        if (Character.isWhitespace(c)) {
            type = WHITESPACE;
            while (++start < end) {
                if (!Character.isWhitespace(buffer[start])) {
                    break;
                }
            }
        } else if (Character.isLetter(c)) {
            type = WORD;
            while (++start < end) {
                c = buffer[start];
                if (Character.isLetter(c) || Character.isDigit(c)) {
                    continue;
                }
                if (c == '-' || c == '\'' || c == '_') {
                    continue;
                }
                break;
            }
        } else if (Character.isDigit(c)) {
            type = NUMBER;
            while (++start < end) {
                c = buffer[start];
                if (!Character.isDigit(c) && c != '.') {
                    break;
                }
            }
        } else if (c >= '!' || c <= '~') {
            type = PUNCTUATION;
            start++;
        } else {
            type = UNRECOGNIZED;
            start++;
        }

        return type;
    }

    public Scanner() {
        tokens = new Token[1];
        gap = 0;
        endgap = 0;
        textLength = 0;
        symbolTable = new HashMap();
        initSymbolTable();
        Symbol endOfText = new Symbol(WHITESPACE, "");
        tokens[0] = new Token(endOfText, 0);
        scanning = false;
        position = 0;
    }

    // Move the gap to a new index within the tokens array.  When preparing to
    // pass a token back to a caller, this is used to ensure that the token's
    // position is relative to the start of the text and not the end.

    private void moveGap(int newgap) {
        if (scanning) {
            throw new Error("moveGap called while scanning");
        }
        if (newgap < 0 || newgap > gap + tokens.length - endgap) {
            throw new Error("bad argument to moveGap");
        }
        if (gap < newgap) {
            while (gap < newgap) {
                tokens[endgap].position += textLength;
                tokens[gap++] = tokens[endgap++];
            }
        } else if (gap > newgap) {
            while (gap > newgap) {
                tokens[--endgap] = tokens[--gap];
                tokens[endgap].position -= textLength;
            }
        }
    }

    /**
     * Find the number of available valid tokens, not counting tokens in or
     * after any area yet to be rescanned.
     */
    public int size() {
        if (scanning) {
            return gap;
        } else {
            return gap + tokens.length - endgap;
        }
    }

    /**
     * Find the n'th token, or null if it is not currently valid.
     */
    public Token getToken(int n) {
        if (n < 0 || n >= gap && scanning) {
            return null;
        }
        if (n >= gap) {
            moveGap(n + 1);
        }
        return tokens[n];
    }

    /**
     * Find the index of the valid token starting before, but nearest to, text
     * position p.  This uses an O(log(n)) binary chop search.
     */
    public int find(int p) {
        int start = 0, end, mid, midpos;
        if (!scanning) {
            moveGap(gap + tokens.length - endgap);
        }
        end = gap - 1;
        if (p > tokens[end].position) {
            return end;
        }
        while (end > start + 1) {
            mid = (start + end) / 2;
            midpos = tokens[mid].position;
            if (p > midpos) {
                start = mid;
            } else {
                end = mid;
            }
        }
        return start;
    }

    /**
     * Report the position of an edit, the length of the text being replaced,
     * and the length of the replacement text, to prepare for rescanning.
     * @return The index of the token at which rescanning will start.
     */
    public int change(int start, int len, int newLen) {
        if (start < 0 || len < 0 || newLen < 0 || start + len > textLength) {
            throw new Error("change(" + start + "," + len + "," + newLen + ")");
        }
        textLength += newLen - len;
        int end = start + newLen;
        if (scanning) {
            while (gap > 0 && tokens[gap - 1].position > start)
                gap--;
            if (gap > 0) {
                gap--;
            }
            if (gap > 0) {
                gap--;
                position = tokens[gap].position;
                state = tokens[gap].symbol.type;
            } else {
                position = 0;
                state = WHITESPACE;
            }
            while (tokens[endgap].position + textLength < end)
                endgap++;
            return gap;
        }
        if (endgap == tokens.length) {
            moveGap(gap - 1);
        }
        scanning = true;
        while (tokens[endgap].position + textLength < start) {
            tokens[endgap].position += textLength;
            tokens[gap++] = tokens[endgap++];
        }
        while (gap > 0 && tokens[gap - 1].position > start) {
            tokens[--endgap] = tokens[--gap];
            tokens[endgap].position -= textLength;
        }
        if (gap > 0) {
            gap--;
        }
        if (gap > 0) {
            gap--;
            position = tokens[gap].position;
            state = tokens[gap].symbol.type;
        } else {
            position = 0;
            state = WHITESPACE;
        }
        while (tokens[endgap].position + textLength < end)
            endgap++;
        return gap;
    }

    /**
     * Find out at what text position any remaining scanning work should
     * start, or -1 if scanning is complete.
     */
    public int position() {
        if (!scanning) {
            return -1;
        } else {
            return position;
        }
    }

    /**
     * Create the initial symbol table.  This can be overridden to enter
     * keywords, for example.  The default implementation does nothing.
     */
    protected void initSymbolTable() {
    }

    // Reuse this symbol object to create each new symbol, then look it up in
    // the symbol table, to replace it by a shared version to minimize space.

    private Symbol symbol = new Symbol(0, null);

    public void setSymbolTable(HashMap symbolTable) {
        this.symbolTable = symbolTable;
    }

    public HashMap getSymbolTable() {
        return symbolTable;
    }

    private boolean caseInsensitiveKeywords = false;
    public boolean isCaseInsensitiveKeywords() {
        return caseInsensitiveKeywords;
    }

    public void setCaseInsensitiveKeywords(boolean caseInsensitiveKeywords) {
        this.caseInsensitiveKeywords = caseInsensitiveKeywords;
    }
    

    /**
     * Lookup a symbol in the symbol table.  This can be overridden to implement
     * keyword detection, for example.  The default implementation just uses the
     * table to ensure that there is only one shared occurrence of each symbol.
     */
    protected Symbol lookup(int type, String name) {
        symbol.type = type;
        symbol.name = name;
        Symbol sym = (Symbol) symbolTable.get(symbol);
        if (sym != null) {
            return sym;
        }
        sym = new Symbol(type, name);
        symbolTable.put(sym, sym);
        return sym;
    }

    /**
     * Scan or rescan a given read-only segment of text.  The segment is assumed
     * to represent a portion of the document starting at
     * <code>position()</code>.  Return the number of tokens successfully
     * scanned, excluding any partial token at the end of the text segment but
     * not at the end of the document.  If the result is 0, the call should be
     * retried with a longer segment.
     */
    public int scan(char[] array, int offset, int length) {
        if (!scanning) {
            throw new Error("scan called when not scanning");
        }
        if (position + length > textLength) {
            throw new Error("scan too much");
        }
        boolean all = position + length == textLength;
        end = start + length;
        int startGap = gap;

        buffer = array;
        start = offset;
        end = start + length;
        while (start < end) {
            int tokenStart = start;
            int type = read();
            if (start == end && !all) {
                break;
            }

            if (type != WHITESPACE) {
                String name =
                        new String(buffer, tokenStart, start - tokenStart);
                Symbol sym = lookup(type, name);
                Token t = new Token(sym, position);
                if (gap >= endgap) {
                    checkCapacity(gap + tokens.length - endgap + 1);
                }
                tokens[gap++] = t;
            }

            // Try to synchronise

            while (tokens[endgap].position + textLength < position)
                endgap++;
            if (position + start - tokenStart == textLength) {
                scanning = false;
            } else if (
                    gap > 0
                    && tokens[endgap].position + textLength == position
                    && tokens[endgap].symbol.type == type) {
                endgap++;
                scanning = false;
                break;
            }
            position += start - tokenStart;
        }
        checkCapacity(gap + tokens.length - endgap);
        return gap - startGap;
    }

    // Change the size of the gap buffer, doubling it if it fills up, and
    // halving if it becomes less than a quarter full.

    private void checkCapacity(int capacity) {
        int oldCapacity = tokens.length;
        if (capacity <= oldCapacity && 4 * capacity >= oldCapacity) {
            return;
        }
        Token[] oldTokens = tokens;
        int newCapacity;
        if (capacity > oldCapacity) {
            newCapacity = oldCapacity * 2;
            if (newCapacity < capacity) {
                newCapacity = capacity;
            }
        } else {
            newCapacity = capacity * 2;
        }

        tokens = new Token[newCapacity];
        System.arraycopy(oldTokens, 0, tokens, 0, gap);
        int n = oldCapacity - endgap;
        System.arraycopy(oldTokens, endgap, tokens, newCapacity - n, n);
        endgap = newCapacity - n;
    }

    void print() {
        for (int i = 0; i < tokens.length; i++) {
            if (i >= gap && i < endgap) {
                continue;
            }
            if (i == endgap) {
                System.out.print("... ");
            }
            System.out.print("" + i + ":" + tokens[i].position);
            System.out.print("-" + (tokens[i].position + tokens[i].symbol.name.length()));
            System.out.print(" ");
        }
        System.out.println();
    }
}
