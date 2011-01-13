/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AAggregateAvgExpr extends PExpr
{
    private TAvg _avg_;
    private TBracketL _bracketL_;
    private PQuery _query_;
    private TBracketR _bracketR_;

    public AAggregateAvgExpr()
    {
    }

    public AAggregateAvgExpr(
        TAvg _avg_,
        TBracketL _bracketL_,
        PQuery _query_,
        TBracketR _bracketR_)
    {
        setAvg(_avg_);

        setBracketL(_bracketL_);

        setQuery(_query_);

        setBracketR(_bracketR_);

    }
    public Object clone()
    {
        return new AAggregateAvgExpr(
            (TAvg) cloneNode(_avg_),
            (TBracketL) cloneNode(_bracketL_),
            (PQuery) cloneNode(_query_),
            (TBracketR) cloneNode(_bracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAggregateAvgExpr(this);
    }

    public TAvg getAvg()
    {
        return _avg_;
    }

    public void setAvg(TAvg node)
    {
        if(_avg_ != null)
        {
            _avg_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _avg_ = node;
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
            + toString(_avg_)
            + toString(_bracketL_)
            + toString(_query_)
            + toString(_bracketR_);
    }

    void removeChild(Node child)
    {
        if(_avg_ == child)
        {
            _avg_ = null;
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
        if(_avg_ == oldChild)
        {
            setAvg((TAvg) newChild);
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
