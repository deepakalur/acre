/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ASubstractionExpr extends PExpr
{
    private PExpr _left_;
    private TMinus _minus_;
    private PExpr _right_;

    public ASubstractionExpr()
    {
    }

    public ASubstractionExpr(
        PExpr _left_,
        TMinus _minus_,
        PExpr _right_)
    {
        setLeft(_left_);

        setMinus(_minus_);

        setRight(_right_);

    }
    public Object clone()
    {
        return new ASubstractionExpr(
            (PExpr) cloneNode(_left_),
            (TMinus) cloneNode(_minus_),
            (PExpr) cloneNode(_right_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASubstractionExpr(this);
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

    public TMinus getMinus()
    {
        return _minus_;
    }

    public void setMinus(TMinus node)
    {
        if(_minus_ != null)
        {
            _minus_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _minus_ = node;
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
            + toString(_minus_)
            + toString(_right_);
    }

    void removeChild(Node child)
    {
        if(_left_ == child)
        {
            _left_ = null;
            return;
        }

        if(_minus_ == child)
        {
            _minus_ = null;
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

        if(_minus_ == oldChild)
        {
            setMinus((TMinus) newChild);
            return;
        }

        if(_right_ == oldChild)
        {
            setRight((PExpr) newChild);
            return;
        }

    }
}