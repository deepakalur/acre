/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AAggregateCountExpr extends PExpr
{
    private TCount _count_;
    private TBracketL _bracketL_;
    private PQuery _query_;
    private TBracketR _bracketR_;

    public AAggregateCountExpr()
    {
    }

    public AAggregateCountExpr(
        TCount _count_,
        TBracketL _bracketL_,
        PQuery _query_,
        TBracketR _bracketR_)
    {
        setCount(_count_);

        setBracketL(_bracketL_);

        setQuery(_query_);

        setBracketR(_bracketR_);

    }
    public Object clone()
    {
        return new AAggregateCountExpr(
            (TCount) cloneNode(_count_),
            (TBracketL) cloneNode(_bracketL_),
            (PQuery) cloneNode(_query_),
            (TBracketR) cloneNode(_bracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAggregateCountExpr(this);
    }

    public TCount getCount()
    {
        return _count_;
    }

    public void setCount(TCount node)
    {
        if(_count_ != null)
        {
            _count_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _count_ = node;
    }

    public TBracketL getBracketL()
    {
        return _bracketL_;
    }

    public void setBracketL(TBracketL node)
    {
        if(_bracketL_ != null)
        {
            _bracketL_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _bracketL_ = node;
    }

    public PQuery getQuery()
    {
        return _query_;
    }

    public void setQuery(PQuery node)
    {
        if(_query_ != null)
        {
            _query_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _query_ = node;
    }

    public TBracketR getBracketR()
    {
        return _bracketR_;
    }

    public void setBracketR(TBracketR node)
    {
        if(_bracketR_ != null)
        {
            _bracketR_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _bracketR_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_count_)
            + toString(_bracketL_)
            + toString(_query_)
            + toString(_bracketR_);
    }

    void removeChild(Node child)
    {
        if(_count_ == child)
        {
            _count_ = null;
            return;
        }

        if(_bracketL_ == child)
        {
            _bracketL_ = null;
            return;
        }

        if(_query_ == child)
        {
            _query_ = null;
            return;
        }

        if(_bracketR_ == child)
        {
            _bracketR_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_count_ == oldChild)
        {
            setCount((TCount) newChild);
            return;
        }

        if(_bracketL_ == oldChild)
        {
            setBracketL((TBracketL) newChild);
            return;
        }

        if(_query_ == oldChild)
        {
            setQuery((PQuery) newChild);
            return;
        }

        if(_bracketR_ == oldChild)
        {
            setBracketR((TBracketR) newChild);
            return;
        }

    }
}
