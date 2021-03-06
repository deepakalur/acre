/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import org.acre.lang.analysis.*;

public final class TAvg extends Token
{
    public TAvg(String text)
    {
        setText(text);
    }

    public TAvg(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TAvg(getText(), getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTAvg(this);
    }
}
