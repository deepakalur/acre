/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AMultiplicationExpr extends PExpr
{
    private PExpr _left_;
    private TStar _star_;
    private PExpr _right_;

    public AMultiplicationExpr()
    {
    }

    public AMultiplicationExpr(
        PExpr _left_,
        TStar _star_,
        PExpr _right_)
    {
        setLeft(_left_);

        setStar(_star_);

        setRight(_right_);

    }
    public Object clone()
    {
        return new AMultiplicationExpr(
            (PExpr) cloneNode(_left_),
            (TStar) cloneNode(_star_),
            (PExpr) cloneNode(_right_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMultiplicationExpr(this);
    }

    public PExpr getLeft()
    {
        return _left_;
    }

    public void setLeft(PExpr node)
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

    public TStar getStar()
    {
        return _star_;
    }

    public void setStar(TStar node)
    {
        if(_star_ != null)
        {
            _star_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _star_ = node;
    }

    public PExpr getRight()
    {
        return _right_;
    }

    public void setRight(PExpr node)
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
            + toString(_star_)
            + toString(_right_);
    }

    void removeChild(Node child)
    {
        if(_left_ == child)
        {
            _left_ = null;
            return;
        }

        if(_star_ == child)
        {
            _star_ = null;
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
            setLeft((PExpr) newChild);
            return;
        }

        if(_star_ == oldChild)
        {
            setStar((TStar) newChild);
            return;
        }

        if(_right_ == oldChild)
        {
            setRight((PExpr) newChild);
            return;
        }

    }
}
