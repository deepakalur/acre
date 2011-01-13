/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AExprRestricted extends PExprRestricted
{
    private PTmpCastExpr _tmpCastExpr_;

    public AExprRestricted()
    {
    }

    public AExprRestricted(
        PTmpCastExpr _tmpCastExpr_)
    {
        setTmpCastExpr(_tmpCastExpr_);

    }
    public Object clone()
    {
        return new AExprRestricted(
            (PTmpCastExpr) cloneNode(_tmpCastExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAExprRestricted(this);
    }

    public PTmpCastExpr getTmpCastExpr()
    {
        return _tmpCastExpr_;
    }

    public void setTmpCastExpr(PTmpCastExpr node)
    {
        if(_tmpCastExpr_ != null)
        {
            _tmpCastExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _tmpCastExpr_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_tmpCastExpr_);
    }

    void removeChild(Node child)
    {
        if(_tmpCastExpr_ == child)
        {
            _tmpCastExpr_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_tmpCastExpr_ == oldChild)
        {
            setTmpCastExpr((PTmpCastExpr) newChild);
            return;
        }

    }
}