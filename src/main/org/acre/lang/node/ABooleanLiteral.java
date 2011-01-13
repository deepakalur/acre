/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ABooleanLiteral extends PLiteral
{
    private PPBoolean _pBoolean_;

    public ABooleanLiteral()
    {
    }

    public ABooleanLiteral(
        PPBoolean _pBoolean_)
    {
        setPBoolean(_pBoolean_);

    }
    public Object clone()
    {
        return new ABooleanLiteral(
            (PPBoolean) cloneNode(_pBoolean_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABooleanLiteral(this);
    }

    public PPBoolean getPBoolean()
    {
        return _pBoolean_;
    }

    public void setPBoolean(PPBoolean node)
    {
        if(_pBoolean_ != null)
        {
            _pBoolean_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _pBoolean_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_pBoolean_);
    }

    void removeChild(Node child)
    {
        if(_pBoolean_ == child)
        {
            _pBoolean_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_pBoolean_ == oldChild)
        {
            setPBoolean((PPBoolean) newChild);
            return;
        }

    }
}