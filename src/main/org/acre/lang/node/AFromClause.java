/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AFromClause extends PFromClause
{
    private TFrom _from_;
    private PFromClauseList _fromClauseList_;

    public AFromClause()
    {
    }

    public AFromClause(
        TFrom _from_,
        PFromClauseList _fromClauseList_)
    {
        setFrom(_from_);

        setFromClauseList(_fromClauseList_);

    }
    public Object clone()
    {
        return new AFromClause(
            (TFrom) cloneNode(_from_),
            (PFromClauseList) cloneNode(_fromClauseList_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFromClause(this);
    }

    public TFrom getFrom()
    {
        return _from_;
    }

    public void setFrom(TFrom node)
    {
        if(_from_ != null)
        {
            _from_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _from_ = node;
    }

    public PFromClauseList getFromClauseList()
    {
        return _fromClauseList_;
    }

    public void setFromClauseList(PFromClauseList node)
    {
        if(_fromClauseList_ != null)
        {
            _fromClauseList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fromClauseList_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_from_)
            + toString(_fromClauseList_);
    }

    void removeChild(Node child)
    {
        if(_from_ == child)
        {
            _from_ = null;
            return;
        }

        if(_fromClauseList_ == child)
        {
            _fromClauseList_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_from_ == oldChild)
        {
            setFrom((TFrom) newChild);
            return;
        }

        if(_fromClauseList_ == oldChild)
        {
            setFromClauseList((PFromClauseList) newChild);
            return;
        }

    }
}
