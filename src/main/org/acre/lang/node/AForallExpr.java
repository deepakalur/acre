/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AForallExpr extends PExpr
{
    private TFor _for_;
    private TAll _all_;
    private PInClause _inClause_;
    private TColon _colon_;
    private PExpr _expr_;

    public AForallExpr()
    {
    }

    public AForallExpr(
        TFor _for_,
        TAll _all_,
        PInClause _inClause_,
        TColon _colon_,
        PExpr _expr_)
    {
        setFor(_for_);

        setAll(_all_);

        setInClause(_inClause_);

        setColon(_colon_);

        setExpr(_expr_);

    }
    public Object clone()
    {
        return new AForallExpr(
            (TFor) cloneNode(_for_),
            (TAll) cloneNode(_all_),
            (PInClause) cloneNode(_inClause_),
            (TColon) cloneNode(_colon_),
            (PExpr) cloneNode(_expr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAForallExpr(this);
    }

    public TFor getFor()
    {
        return _for_;
    }

    public void setFor(TFor node)
    {
        if(_for_ != null)
        {
            _for_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _for_ = node;
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

    public PInClause getInClause()
    {
        return _inClause_;
    }

    public void setInClause(PInClause node)
    {
        if(_inClause_ != null)
        {
            _inClause_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _inClause_ = node;
    }

    public TColon getColon()
    {
        return _colon_;
    }

    public void setColon(TColon node)
    {
        if(_colon_ != null)
        {
            _colon_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _colon_ = node;
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

    public String toString()
    {
        return ""
            + toString(_for_)
            + toString(_all_)
            + toString(_inClause_)
            + toString(_colon_)
            + toString(_expr_);
    }

    void removeChild(Node child)
    {
        if(_for_ == child)
        {
            _for_ = null;
            return;
        }

        if(_all_ == child)
        {
            _all_ = null;
            return;
        }

        if(_inClause_ == child)
        {
            _inClause_ = null;
            return;
        }

        if(_colon_ == child)
        {
            _colon_ = null;
            return;
        }

        if(_expr_ == child)
        {
            _expr_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_for_ == oldChild)
        {
            setFor((TFor) newChild);
            return;
        }

        if(_all_ == oldChild)
        {
            setAll((TAll) newChild);
            return;
        }

        if(_inClause_ == oldChild)
        {
            setInClause((PInClause) newChild);
            return;
        }

        if(_colon_ == oldChild)
        {
            setColon((TColon) newChild);
            return;
        }

        if(_expr_ == oldChild)
        {
            setExpr((PExpr) newChild);
            return;
        }

    }
}
