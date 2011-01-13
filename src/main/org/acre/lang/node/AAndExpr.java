/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AAndExpr extends PExpr
{
    private PExpr _left_;
    private TAnd _and_;
    private PExpr _right_;

    public AAndExpr()
    {
    }

    public AAndExpr(
        PExpr _left_,
        TAnd _and_,
        PExpr _right_)
    {
        setLeft(_left_);

        setAnd(_and_);

        setRight(_right_);

    }
    public Object clone()
    {
        return new AAndExpr(
            (PExpr) cloneNode(_left_),
            (TAnd) cloneNode(_and_),
            (PExpr) cloneNode(_right_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAndExpr(this);
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

    public TAnd getAnd()
    {
        return _and_;
    }

    public void setAnd(TAnd node)
    {
        if(_and_ != null)
        {
            _and_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _and_ = node;
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
            + toString(_and_)
            + toString(_right_);
    }

    void removeChild(Node child)
    {
        if(_left_ == child)
        {
            _left_ = null;
            return;
        }

        if(_and_ == child)
        {
            _and_ = null;
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

        if(_and_ == oldChild)
        {
            setAnd((TAnd) newChild);
            return;
        }

        if(_right_ == oldChild)
        {
            setRight((PExpr) newChild);
            return;
        }

    }
}
