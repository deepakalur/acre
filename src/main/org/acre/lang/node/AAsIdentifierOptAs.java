/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AAsIdentifierOptAs extends PAsIdentifierOptAs
{
    private TAs _as_;
    private TIdentifier _identifier_;

    public AAsIdentifierOptAs()
    {
    }

    public AAsIdentifierOptAs(
        TAs _as_,
        TIdentifier _identifier_)
    {
        setAs(_as_);

        setIdentifier(_identifier_);

    }
    public Object clone()
    {
        return new AAsIdentifierOptAs(
            (TAs) cloneNode(_as_),
            (TIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAsIdentifierOptAs(this);
    }

    public TAs getAs()
    {
        return _as_;
    }

    public void setAs(TAs node)
    {
        if(_as_ != null)
        {
            _as_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _as_ = node;
    }

    public TIdentifier getIdentifier()
    {
        return _identifier_;
    }

    public void setIdentifier(TIdentifier node)
    {
        if(_identifier_ != null)
        {
            _identifier_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _identifier_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_as_)
            + toString(_identifier_);
    }

    void removeChild(Node child)
    {
        if(_as_ == child)
        {
            _as_ = null;
            return;
        }

        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_as_ == oldChild)
        {
            setAs((TAs) newChild);
            return;
        }

        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

    }
}