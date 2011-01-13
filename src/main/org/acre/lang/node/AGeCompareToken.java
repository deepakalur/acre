/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AGeCompareToken extends PCompareToken
{
    private TGe _ge_;

    public AGeCompareToken()
    {
    }

    public AGeCompareToken(
        TGe _ge_)
    {
        setGe(_ge_);

    }
    public Object clone()
    {
        return new AGeCompareToken(
            (TGe) cloneNode(_ge_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAGeCompareToken(this);
    }

    public TGe getGe()
    {
        return _ge_;
    }

    public void setGe(TGe node)
    {
        if(_ge_ != null)
        {
            _ge_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _ge_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_ge_);
    }

    void removeChild(Node child)
    {
        if(_ge_ == child)
        {
            _ge_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_ge_ == oldChild)
        {
            setGe((TGe) newChild);
            return;
        }

    }
}