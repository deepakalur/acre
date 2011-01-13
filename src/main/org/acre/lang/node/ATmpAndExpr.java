/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ATmpAndExpr extends PTmpAndExpr
{
    private PTmpAndExpr _tmpAndExpr_;
    private TAnd _and_;
    private PQuantifierExpr _quantifierExpr_;

    public ATmpAndExpr()
    {
    }

    public ATmpAndExpr(
        PTmpAndExpr _tmpAndExpr_,
        TAnd _and_,
        PQuantifierExpr _quantifierExpr_)
    {
        setTmpAndExpr(_tmpAndExpr_);

        setAnd(_and_);

        setQuantifierExpr(_quantifierExpr_);

    }
    public Object clone()
    {
        return new ATmpAndExpr(
            (PTmpAndExpr) cloneNode(_tmpAndExpr_),
            (TAnd) cloneNode(_and_),
            (PQuantifierExpr) cloneNode(_quantifierExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseATmpAndExpr(this);
    }

    public PTmpAndExpr getTmpAndExpr()
    {
        return _tmpAndExpr_;
    }

    public void setTmpAndExpr(PTmpAndExpr node)
    {
        if(_tmpAndExpr_ != null)
        {
            _tmpAndExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _tmpAndExpr_ = node;
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

    public PQuantifierExpr getQuantifierExpr()
    {
        return _quantifierExpr_;
    }

    public void setQuantifierExpr(PQuantifierExpr node)
    {
        if(_quantifierExpr_ != null)
        {
            _quantifierExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _quantifierExpr_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_tmpAndExpr_)
            + toString(_and_)
            + toString(_quantifierExpr_);
    }

    void removeChild(Node child)
    {
        if(_tmpAndExpr_ == child)
        {
            _tmpAndExpr_ = null;
            return;
        }

        if(_and_ == child)
        {
            _and_ = null;
            return;
        }

        if(_quantifierExpr_ == child)
        {
            _quantifierExpr_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_tmpAndExpr_ == oldChild)
        {
            setTmpAndExpr((PTmpAndExpr) newChild);
            return;
        }

        if(_and_ == oldChild)
        {
            setAnd((TAnd) newChild);
            return;
        }

        if(_quantifierExpr_ == oldChild)
        {
            setQuantifierExpr((PQuantifierExpr) newChild);
            return;
        }

    }
}
