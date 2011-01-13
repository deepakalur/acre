/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ATmpOrelseExpr extends PTmpOrelseExpr
{
    private PTmpOrelseExpr _tmpOrelseExpr_;
    private TOrelse _orelse_;
    private PTmpAndExpr _tmpAndExpr_;

    public ATmpOrelseExpr()
    {
    }

    public ATmpOrelseExpr(
        PTmpOrelseExpr _tmpOrelseExpr_,
        TOrelse _orelse_,
        PTmpAndExpr _tmpAndExpr_)
    {
        setTmpOrelseExpr(_tmpOrelseExpr_);

        setOrelse(_orelse_);

        setTmpAndExpr(_tmpAndExpr_);

    }
    public Object clone()
    {
        return new ATmpOrelseExpr(
            (PTmpOrelseExpr) cloneNode(_tmpOrelseExpr_),
            (TOrelse) cloneNode(_orelse_),
            (PTmpAndExpr) cloneNode(_tmpAndExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseATmpOrelseExpr(this);
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

    public String toString()
    {
        return ""
            + toString(_tmpOrelseExpr_)
            + toString(_orelse_)
            + toString(_tmpAndExpr_);
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

        if(_tmpAndExpr_ == child)
        {
            _tmpAndExpr_ = null;
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

        if(_tmpAndExpr_ == oldChild)
        {
            setTmpAndExpr((PTmpAndExpr) newChild);
            return;
        }

    }
}
