/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import org.acre.lang.analysis.*;

public final class TSqBracketL extends Token
{
    public TSqBracketL()
    {
        super.setText("[");
    }

    public TSqBracketL(int line, int pos)
    {
        super.setText("[");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TSqBracketL(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTSqBracketL(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TSqBracketL text.");
    }
}
