/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AIdentifierQuery extends PQuery
{
    private TIdentifier _identifier_;

    public AIdentifierQuery()
    {
    }

    public AIdentifierQuery(
        TIdentifier _identifier_)
    {
        setIdentifier(_identifier_);

    }
    public Object clone()
    {
        return new AIdentifierQuery(
            (TIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIdentifierQuery(this);
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
            + toString(_identifier_);
    }

    void removeChild(Node child)
    {
        if(_identifier_ == child)
        {
            _identifier_ = null;
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

    }
}
