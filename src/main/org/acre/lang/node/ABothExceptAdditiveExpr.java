/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ABothExceptAdditiveExpr extends PAdditiveExpr
{
    private TIdentifier _left_;
    private TExcept _except_;
    private TIdentifier _right_;

    public ABothExceptAdditiveExpr()
    {
    }

    public ABothExceptAdditiveExpr(
        TIdentifier _left_,
        TExcept _except_,
        TIdentifier _right_)
    {
        setLeft(_left_);

        setExcept(_except_);

        setRight(_right_);

    }
    public Object clone()
    {
        return new ABothExceptAdditiveExpr(
            (TIdentifier) cloneNode(_left_),
            (TExcept) cloneNode(_except_),
            (TIdentifier) cloneNode(_right_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABothExceptAdditiveExpr(this);
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

    public TExcept getExcept()
    {
        return _except_;
    }

    public void setExcept(TExcept node)
    {
        if(_except_ != null)
        {
            _except_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _except_ = node;
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
            + toString(_except_)
            + toString(_right_);
    }

    void removeChild(Node child)
    {
        if(_left_ == child)
        {
            _left_ = null;
            return;
        }

        if(_except_ == child)
        {
            _except_ = null;
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

        if(_except_ == oldChild)
        {
            setExcept((TExcept) newChild);
            return;
        }

        if(_right_ == oldChild)
        {
            setRight((TIdentifier) newChild);
            return;
        }

    }
}
