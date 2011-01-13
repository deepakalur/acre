/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AMinAggregateExpr extends PAggregateExpr
{
    private TMin _min_;
    private TBracketL _bracketL_;
    private PQueryRestricted _queryRestricted_;
    private TBracketR _bracketR_;

    public AMinAggregateExpr()
    {
    }

    public AMinAggregateExpr(
        TMin _min_,
        TBracketL _bracketL_,
        PQueryRestricted _queryRestricted_,
        TBracketR _bracketR_)
    {
        setMin(_min_);

        setBracketL(_bracketL_);

        setQueryRestricted(_queryRestricted_);

        setBracketR(_bracketR_);

    }
    public Object clone()
    {
        return new AMinAggregateExpr(
            (TMin) cloneNode(_min_),
            (TBracketL) cloneNode(_bracketL_),
            (PQueryRestricted) cloneNode(_queryRestricted_),
            (TBracketR) cloneNode(_bracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMinAggregateExpr(this);
    }

    public TMin getMin()
    {
        return _min_;
    }

    public void setMin(TMin node)
    {
        if(_min_ != null)
        {
            _min_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _min_ = node;
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

    public PQueryRestricted getQueryRestricted()
    {
        return _queryRestricted_;
    }

    public void setQueryRestricted(PQueryRestricted node)
    {
        if(_queryRestricted_ != null)
        {
            _queryRestricted_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _queryRestricted_ = node;
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
            + toString(_min_)
            + toString(_bracketL_)
            + toString(_queryRestricted_)
            + toString(_bracketR_);
    }

    void removeChild(Node child)
    {
        if(_min_ == child)
        {
            _min_ = null;
            return;
        }

        if(_bracketL_ == child)
        {
            _bracketL_ = null;
            return;
        }

        if(_queryRestricted_ == child)
        {
            _queryRestricted_ = null;
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
        if(_min_ == oldChild)
        {
            setMin((TMin) newChild);
            return;
        }

        if(_bracketL_ == oldChild)
        {
            setBracketL((TBracketL) newChild);
            return;
        }

        if(_queryRestricted_ == oldChild)
        {
            setQueryRestricted((PQueryRestricted) newChild);
            return;
        }

        if(_bracketR_ == oldChild)
        {
            setBracketR((TBracketR) newChild);
            return;
        }

    }
}
