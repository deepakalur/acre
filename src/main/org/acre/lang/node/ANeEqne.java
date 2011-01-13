/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ANeEqne extends PEqne
{
    private TNe _ne_;

    public ANeEqne()
    {
    }

    public ANeEqne(
        TNe _ne_)
    {
        setNe(_ne_);

    }
    public Object clone()
    {
        return new ANeEqne(
            (TNe) cloneNode(_ne_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseANeEqne(this);
    }

    public TNe getNe()
    {
        return _ne_;
    }

    public void setNe(TNe node)
    {
        if(_ne_ != null)
        {
            _ne_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _ne_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_ne_);
    }

    void removeChild(Node child)
    {
        if(_ne_ == child)
        {
            _ne_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_ne_ == oldChild)
        {
            setNe((TNe) newChild);
            return;
        }

    }
}