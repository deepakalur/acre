/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import org.acre.lang.analysis.*;

public final class TArrow extends Token
{
    public TArrow()
    {
        super.setText("->");
    }

    public TArrow(int line, int pos)
    {
        super.setText("->");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TArrow(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTArrow(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TArrow text.");
    }
}
