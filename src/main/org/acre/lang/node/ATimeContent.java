/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ATimeContent extends PTimeContent
{
    private TLongLiteral _hour_;
    private TColon _q1_;
    private TLongLiteral _minute_;
    private TColon _q2_;
    private TLongLiteral _second_;

    public ATimeContent()
    {
    }

    public ATimeContent(
        TLongLiteral _hour_,
        TColon _q1_,
        TLongLiteral _minute_,
        TColon _q2_,
        TLongLiteral _second_)
    {
        setHour(_hour_);

        setQ1(_q1_);

        setMinute(_minute_);

        setQ2(_q2_);

        setSecond(_second_);

    }
    public Object clone()
    {
        return new ATimeContent(
            (TLongLiteral) cloneNode(_hour_),
            (TColon) cloneNode(_q1_),
            (TLongLiteral) cloneNode(_minute_),
            (TColon) cloneNode(_q2_),
            (TLongLiteral) cloneNode(_second_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseATimeContent(this);
    }

    public TLongLiteral getHour()
    {
        return _hour_;
    }

    public void setHour(TLongLiteral node)
    {
        if(_hour_ != null)
        {
            _hour_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _hour_ = node;
    }

    public TColon getQ1()
    {
        return _q1_;
    }

    public void setQ1(TColon node)
    {
        if(_q1_ != null)
        {
            _q1_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _q1_ = node;
    }

    public TLongLiteral getMinute()
    {
        return _minute_;
    }

    public void setMinute(TLongLiteral node)
    {
        if(_minute_ != null)
        {
            _minute_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _minute_ = node;
    }

    public TColon getQ2()
    {
        return _q2_;
    }

    public void setQ2(TColon node)
    {
        if(_q2_ != null)
        {
            _q2_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _q2_ = node;
    }

    public TLongLiteral getSecond()
    {
        return _second_;
    }

    public void setSecond(TLongLiteral node)
    {
        if(_second_ != null)
        {
            _second_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _second_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_hour_)
            + toString(_q1_)
            + toString(_minute_)
            + toString(_q2_)
            + toString(_second_);
    }

    void removeChild(Node child)
    {
        if(_hour_ == child)
        {
            _hour_ = null;
            return;
        }

        if(_q1_ == child)
        {
            _q1_ = null;
            return;
        }

        if(_minute_ == child)
        {
            _minute_ = null;
            return;
        }

        if(_q2_ == child)
        {
            _q2_ = null;
            return;
        }

        if(_second_ == child)
        {
            _second_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_hour_ == oldChild)
        {
            setHour((TLongLiteral) newChild);
            return;
        }

        if(_q1_ == oldChild)
        {
            setQ1((TColon) newChild);
            return;
        }

        if(_minute_ == oldChild)
        {
            setMinute((TLongLiteral) newChild);
            return;
        }

        if(_q2_ == oldChild)
        {
            setQ2((TColon) newChild);
            return;
        }

        if(_second_ == oldChild)
        {
            setSecond((TLongLiteral) newChild);
            return;
        }

    }
}
