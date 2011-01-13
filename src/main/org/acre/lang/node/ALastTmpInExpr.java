/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ALastTmpInExpr extends PTmpInExpr
{
    private PTmpInExpr _tmpInExpr_;
    private TNot _not_;
    private TIn _in_;
    private TIdentifier _identifier_;

    public ALastTmpInExpr()
    {
    }

    public ALastTmpInExpr(
        PTmpInExpr _tmpInExpr_,
        TNot _not_,
        TIn _in_,
        TIdentifier _identifier_)
    {
        setTmpInExpr(_tmpInExpr_);

        setNot(_not_);

        setIn(_in_);

        setIdentifier(_identifier_);

    }
    public Object clone()
    {
        return new ALastTmpInExpr(
            (PTmpInExpr) cloneNode(_tmpInExpr_),
            (TNot) cloneNode(_not_),
            (TIn) cloneNode(_in_),
            (TIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALastTmpInExpr(this);
    }

    public PTmpInExpr getTmpInExpr()
    {
        return _tmpInExpr_;
    }

    public void setTmpInExpr(PTmpInExpr node)
    {
        if(_tmpInExpr_ != null)
        {
            _tmpInExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _tmpInExpr_ = node;
    }

    public TNot getNot()
    {
        return _not_;
    }

    public void setNot(TNot node)
    {
        if(_not_ != null)
        {
            _not_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _not_ = node;
    }

    public TIn getIn()
    {
        return _in_;
    }

    public void setIn(TIn node)
    {
        if(_in_ != null)
        {
            _in_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _in_ = node;
    }

    public TIdentifier getIdentifier()
    {
        return _identifier_;
    }

    public void setIdentifier(TIdentifier node)
    {
        if(_identifier_ != null)
        {
            _identifier_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _identifier_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_tmpInExpr_)
            + toString(_not_)
            + toString(_in_)
            + toString(_identifier_);
    }

    void removeChild(Node child)
    {
        if(_tmpInExpr_ == child)
        {
            _tmpInExpr_ = null;
            return;
        }

        if(_not_ == child)
        {
            _not_ = null;
            return;
        }

        if(_in_ == child)
        {
            _in_ = null;
            return;
        }

        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_tmpInExpr_ == oldChild)
        {
            setTmpInExpr((PTmpInExpr) newChild);
            return;
        }

        if(_not_ == oldChild)
        {
            setNot((TNot) newChild);
            return;
        }

        if(_in_ == oldChild)
        {
            setIn((TIn) newChild);
            return;
        }

        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

    }
}