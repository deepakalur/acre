/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ATmpWhereClause extends PWhereClause
{
    private TWhere _where_;
    private TAll _all_;
    private PExprRestricted _exprRestricted_;

    public ATmpWhereClause()
    {
    }

    public ATmpWhereClause(
        TWhere _where_,
        TAll _all_,
        PExprRestricted _exprRestricted_)
    {
        setWhere(_where_);

        setAll(_all_);

        setExprRestricted(_exprRestricted_);

    }
    public Object clone()
    {
        return new ATmpWhereClause(
            (TWhere) cloneNode(_where_),
            (TAll) cloneNode(_all_),
            (PExprRestricted) cloneNode(_exprRestricted_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseATmpWhereClause(this);
    }

    public TWhere getWhere()
    {
        return _where_;
    }

    public void setWhere(TWhere node)
    {
        if(_where_ != null)
        {
            _where_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _where_ = node;
    }

    public TAll getAll()
    {
        return _all_;
    }

    public void setAll(TAll node)
    {
        if(_all_ != null)
        {
            _all_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _all_ = node;
    }

    public PExprRestricted getExprRestricted()
    {
        return _exprRestricted_;
    }

    public void setExprRestricted(PExprRestricted node)
    {
        if(_exprRestricted_ != null)
        {
            _exprRestricted_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _exprRestricted_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_where_)
            + toString(_all_)
            + toString(_exprRestricted_);
    }

    void removeChild(Node child)
    {
        if(_where_ == child)
        {
            _where_ = null;
            return;
        }

        if(_all_ == child)
        {
            _all_ = null;
            return;
        }

        if(_exprRestricted_ == child)
        {
            _exprRestricted_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_where_ == oldChild)
        {
            setWhere((TWhere) newChild);
            return;
        }

        if(_all_ == oldChild)
        {
            setAll((TAll) newChild);
            return;
        }

        if(_exprRestricted_ == oldChild)
        {
            setExprRestricted((PExprRestricted) newChild);
            return;
        }

    }
}
