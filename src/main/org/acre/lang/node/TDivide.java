/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import org.acre.lang.analysis.*;

public final class TDivide extends Token
{
    public TDivide()
    {
        super.setText("/");
    }

    public TDivide(int line, int pos)
    {
        super.setText("/");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TDivide(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTDivide(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TDivide text.");
    }
}
