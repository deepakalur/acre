/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ABothBinorAdditiveExpr extends PAdditiveExpr
{
    private TIdentifier _left_;
    private TBinor _binor_;
    private TIdentifier _right_;

    public ABothBinorAdditiveExpr()
    {
    }

    public ABothBinorAdditiveExpr(
        TIdentifier _left_,
        TBinor _binor_,
        TIdentifier _right_)
    {
        setLeft(_left_);

        setBinor(_binor_);

        setRight(_right_);

    }
    public Object clone()
    {
        return new ABothBinorAdditiveExpr(
            (TIdentifier) cloneNode(_left_),
            (TBinor) cloneNode(_binor_),
            (TIdentifier) cloneNode(_right_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABothBinorAdditiveExpr(this);
    }

    public TIdentifier getLeft()
    {
        return _left_;
    }

    public void setLeft(TIdentifier node)
    {
        if(_left_ != null)
        {
            _left_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _left_ = node;
    }

    public TBinor getBinor()
    {
        return _binor_;
    }

    public void setBinor(TBinor node)
    {
        if(_binor_ != null)
        {
            _binor_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _binor_ = node;
    }

    public TIdentifier getRight()
    {
        return _right_;
    }

    public void setRight(TIdentifier node)
    {
        if(_right_ != null)
        {
            _right_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _right_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_left_)
            + toString(_binor_)
            + toString(_right_);
    }

    void removeChild(Node child)
    {
        if(_left_ == child)
        {
            _left_ = null;
            return;
        }

        if(_binor_ == child)
        {
            _binor_ = null;
            return;
        }

        if(_right_ == child)
        {
            _right_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_left_ == oldChild)
        {
            setLeft((TIdentifier) newChild);
            return;
        }

        if(_binor_ == oldChild)
        {
            setBinor((TBinor) newChild);
            return;
        }

        if(_right_ == oldChild)
        {
            setRight((TIdentifier) newChild);
            return;
        }

    }
}
