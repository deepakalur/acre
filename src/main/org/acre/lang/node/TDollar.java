/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import org.acre.lang.analysis.*;

public final class TDollar extends Token
{
    public TDollar()
    {
        super.setText("$");
    }

    public TDollar(int line, int pos)
    {
        super.setText("$");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TDollar(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTDollar(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TDollar text.");
    }
}