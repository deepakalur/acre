/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ASomeCompositePredicate extends PCompositePredicate
{
    private TSome _some_;

    public ASomeCompositePredicate()
    {
    }

    public ASomeCompositePredicate(
        TSome _some_)
    {
        setSome(_some_);

    }
    public Object clone()
    {
        return new ASomeCompositePredicate(
            (TSome) cloneNode(_some_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASomeCompositePredicate(this);
    }

    public TSome getSome()
    {
        return _some_;
    }

    public void setSome(TSome node)
    {
        if(_some_ != null)
        {
            _some_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _some_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_some_);
    }

    void removeChild(Node child)
    {
        if(_some_ == child)
        {
            _some_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_some_ == oldChild)
        {
            setSome((TSome) newChild);
            return;
        }

    }
}