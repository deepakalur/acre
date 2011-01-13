/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import org.acre.lang.analysis.*;

public final class TBracketL extends Token
{
    public TBracketL()
    {
        super.setText("(");
    }

    public TBracketL(int line, int pos)
    {
        super.setText("(");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TBracketL(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTBracketL(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TBracketL text.");
    }
}
