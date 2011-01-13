/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ATmpIdentifierProjection extends PProjection
{
    private TIdentifier _identifier_;
    private PAsIdentifier _asIdentifier_;

    public ATmpIdentifierProjection()
    {
    }

    public ATmpIdentifierProjection(
        TIdentifier _identifier_,
        PAsIdentifier _asIdentifier_)
    {
        setIdentifier(_identifier_);

        setAsIdentifier(_asIdentifier_);

    }
    public Object clone()
    {
        return new ATmpIdentifierProjection(
            (TIdentifier) cloneNode(_identifier_),
            (PAsIdentifier) cloneNode(_asIdentifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseATmpIdentifierProjection(this);
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

    public PAsIdentifier getAsIdentifier()
    {
        return _asIdentifier_;
    }

    public void setAsIdentifier(PAsIdentifier node)
    {
        if(_asIdentifier_ != null)
        {
            _asIdentifier_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _asIdentifier_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_identifier_)
            + toString(_asIdentifier_);
    }

    void removeChild(Node child)
    {
        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

        if(_asIdentifier_ == child)
        {
            _asIdentifier_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

        if(_asIdentifier_ == oldChild)
        {
            setAsIdentifier((PAsIdentifier) newChild);
            return;
        }

    }
}