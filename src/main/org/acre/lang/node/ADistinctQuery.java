/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ADistinctQuery extends PQuery
{
    private TDistinct _distinct_;
    private TBracketL _bracketL_;
    private PQueryRestricted _queryRestricted_;
    private TBracketR _bracketR_;

    public ADistinctQuery()
    {
    }

    public ADistinctQuery(
        TDistinct _distinct_,
        TBracketL _bracketL_,
        PQueryRestricted _queryRestricted_,
        TBracketR _bracketR_)
    {
        setDistinct(_distinct_);

        setBracketL(_bracketL_);

        setQueryRestricted(_queryRestricted_);

        setBracketR(_bracketR_);

    }
    public Object clone()
    {
        return new ADistinctQuery(
            (TDistinct) cloneNode(_distinct_),
            (TBracketL) cloneNode(_bracketL_),
            (PQueryRestricted) cloneNode(_queryRestricted_),
            (TBracketR) cloneNode(_bracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseADistinctQuery(this);
    }

    public TDistinct getDistinct()
    {
        return _distinct_;
    }

    public void setDistinct(TDistinct node)
    {
        if(_distinct_ != null)
        {
            _distinct_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _distinct_ = node;
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
            + toString(_distinct_)
            + toString(_bracketL_)
            + toString(_queryRestricted_)
            + toString(_bracketR_);
    }

    void removeChild(Node child)
    {
        if(_distinct_ == child)
        {
            _distinct_ = null;
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
        if(_distinct_ == oldChild)
        {
            setDistinct((TDistinct) newChild);
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
