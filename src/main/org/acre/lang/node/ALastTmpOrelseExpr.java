/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ALastTmpOrelseExpr extends PTmpOrelseExpr
{
    private PTmpOrelseExpr _tmpOrelseExpr_;
    private TOrelse _orelse_;
    private TIdentifier _identifier_;

    public ALastTmpOrelseExpr()
    {
    }

    public ALastTmpOrelseExpr(
        PTmpOrelseExpr _tmpOrelseExpr_,
        TOrelse _orelse_,
        TIdentifier _identifier_)
    {
        setTmpOrelseExpr(_tmpOrelseExpr_);

        setOrelse(_orelse_);

        setIdentifier(_identifier_);

    }
    public Object clone()
    {
        return new ALastTmpOrelseExpr(
            (PTmpOrelseExpr) cloneNode(_tmpOrelseExpr_),
            (TOrelse) cloneNode(_orelse_),
            (TIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALastTmpOrelseExpr(this);
    }

    public PTmpOrelseExpr getTmpOrelseExpr()
    {
        return _tmpOrelseExpr_;
    }

    public void setTmpOrelseExpr(PTmpOrelseExpr node)
    {
        if(_tmpOrelseExpr_ != null)
        {
            _tmpOrelseExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _tmpOrelseExpr_ = node;
    }

    public TOrelse getOrelse()
    {
        return _orelse_;
    }

    public void setOrelse(TOrelse node)
    {
        if(_orelse_ != null)
        {
            _orelse_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _orelse_ = node;
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
            + toString(_tmpOrelseExpr_)
            + toString(_orelse_)
            + toString(_identifier_);
    }

    void removeChild(Node child)
    {
        if(_tmpOrelseExpr_ == child)
        {
            _tmpOrelseExpr_ = null;
            return;
        }

        if(_orelse_ == child)
        {
            _orelse_ = null;
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
        if(_tmpOrelseExpr_ == oldChild)
        {
            setTmpOrelseExpr((PTmpOrelseExpr) newChild);
            return;
        }

        if(_orelse_ == oldChild)
        {
            setOrelse((TOrelse) newChild);
            return;
        }

        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

    }
}
