/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AIndexExpr extends PExpr
{
    private PExpr _expr_;
    private TSqBracketL _sqBracketL_;
    private PIndex _index_;
    private TSqBracketR _sqBracketR_;

    public AIndexExpr()
    {
    }

    public AIndexExpr(
        PExpr _expr_,
        TSqBracketL _sqBracketL_,
        PIndex _index_,
        TSqBracketR _sqBracketR_)
    {
        setExpr(_expr_);

        setSqBracketL(_sqBracketL_);

        setIndex(_index_);

        setSqBracketR(_sqBracketR_);

    }
    public Object clone()
    {
        return new AIndexExpr(
            (PExpr) cloneNode(_expr_),
            (TSqBracketL) cloneNode(_sqBracketL_),
            (PIndex) cloneNode(_index_),
            (TSqBracketR) cloneNode(_sqBracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIndexExpr(this);
    }

    public PExpr getExpr()
    {
        return _expr_;
    }

    public void setExpr(PExpr node)
    {
        if(_expr_ != null)
        {
            _expr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _expr_ = node;
    }

    public TSqBracketL getSqBracketL()
    {
        return _sqBracketL_;
    }

    public void setSqBracketL(TSqBracketL node)
    {
        if(_sqBracketL_ != null)
        {
            _sqBracketL_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _sqBracketL_ = node;
    }

    public PIndex getIndex()
    {
        return _index_;
    }

    public void setIndex(PIndex node)
    {
        if(_index_ != null)
        {
            _index_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _index_ = node;
    }

    public TSqBracketR getSqBracketR()
    {
        return _sqBracketR_;
    }

    public void setSqBracketR(TSqBracketR node)
    {
        if(_sqBracketR_ != null)
        {
            _sqBracketR_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _sqBracketR_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_expr_)
            + toString(_sqBracketL_)
            + toString(_index_)
            + toString(_sqBracketR_);
    }

    void removeChild(Node child)
    {
        if(_expr_ == child)
        {
            _expr_ = null;
            return;
        }

        if(_sqBracketL_ == child)
        {
            _sqBracketL_ = null;
            return;
        }

        if(_index_ == child)
        {
            _index_ = null;
            return;
        }

        if(_sqBracketR_ == child)
        {
            _sqBracketR_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_expr_ == oldChild)
        {
            setExpr((PExpr) newChild);
            return;
        }

        if(_sqBracketL_ == oldChild)
        {
            setSqBracketL((TSqBracketL) newChild);
            return;
        }

        if(_index_ == oldChild)
        {
            setIndex((PIndex) newChild);
            return;
        }

        if(_sqBracketR_ == oldChild)
        {
            setSqBracketR((TSqBracketR) newChild);
            return;
        }

    }
}
