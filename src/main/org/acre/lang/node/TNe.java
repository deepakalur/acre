/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import org.acre.lang.analysis.*;

public final class TNe extends Token
{
    public TNe()
    {
        super.setText("!=");
    }

    public TNe(int line, int pos)
    {
        super.setText("!=");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TNe(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTNe(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TNe text.");
    }
}
