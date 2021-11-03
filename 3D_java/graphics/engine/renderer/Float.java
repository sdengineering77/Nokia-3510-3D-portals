package graphics.engine.renderer;

import java.lang.*;
/**
 * <p>Title: Class for float-point calculations in J2ME applications (MIDP 1.0 CLDC 1.0 where float or double types not exist)</p>
 * <p>Description: It makes float-point calculations via integer values</p>
 * <p>Copyright: Nick Henson Copyright (c) 2002-2003</p>
 * <p>Company: UNTEH</p>
 * <p>License: Free use only for non-commercial purpose</p>
 * <p>If you want to use all or part of this class for commercial applications then have in view of these condition:</p>
 * <p>1. I need a one copy of your product which includes my class with license key and so on</p>
 * <p>2. Please append my copyright information henson.midp.Float (C) by Nikolay Klimchuk on ‘About’ screen of your product</p>
 * <p>3. If you have web site please append link <a href=”http://henson.newmail.ru”>Nikolay Klimchuk</a> on the page with description of your product</p>
 * <p>That's all, thank you!</p>
 * @author Nikolay Klimchuk http://henson.newmail.ru
 * @version 0.7
 * 
 * 
 * THANX A LOTTA!! gr Danny
 */

public class Float
{
  // Math section
  final static Float ERROR=new Float(-93242L);
  // Number of itterations, if you want to make calculations more precise set ITNUM=6,7,...
  // or ITMUN=4,3,... to make it faster
  final static int ITNUM=4;
  // Square root from 3
  final static Float SQRT3=new Float(1732050807568877294L, -18L);
  // Pi constant
  final static public Float PI=new Float(3141592653589793238L, -18L);
  final static public Float ZERO=new Float();
  final static public Float ONE=new Float(1L);
  //
  final static public Float PIdiv2=PI.Div(2L);
  final static public Float PIdiv4=PIdiv2.Div(2L);
  final static public Float PIdiv6=PIdiv2.Div(3L);
  final static public Float PIdiv12=PIdiv6.Div(2L);
  final static public Float PImul2=PI.Mul(2L);
  final static public Float PImul4=PI.Mul(4L);
  //
  public long m_Val;
  public long m_E;
  //
  public Float()
  {
    m_Val=m_E=0;
  }
  public Float(long value)
  {
    m_Val=value;
    m_E=0;
  }
  public Float(long value, long e)
  {
    m_Val=value;
    if(m_Val==0)
      m_E=0;
    else
      m_E=e;
  }
  public Float(Float value)
  {
    m_Val=value.m_Val;
    if(m_Val==0)
      m_E=0;
    else
      m_E=value.m_E;
  }
  public long toLong()
  {
    long tmpE=m_E;
    long tmpVal=m_Val;
    //
    while(tmpE!=0)
    {
      if(tmpE<0)
      {
        tmpVal/=10;
        tmpE++;
      }
      else
      {
        tmpVal*=10;
        tmpE--;
      }
    }
    return tmpVal;
  }
  public String toShortString()
  {
    Long l=new Long(m_Val);
    String str=l.toString();
    int len=str.length()+(int)m_E;
    if(m_Val<0L)
    {
      if(len>1)
          return str.substring(0, len);
    }
    else
    {
      if(len>0)
        return str.substring(0, len);
    }
    //
    return "0";
  }
  public String toString()
  {
    if(this.Equal(ERROR))
      return "NaN";
    //
    Long l=new Long(m_Val);
    String str=l.toString();
    int len=str.length();
    boolean neg=false;
    if(m_Val<0L)
    {
      neg=true;
      str=str.substring(1, len);
      len--;
    }
    if(m_E<0L)
    {
      int absE=(int)Math.abs(m_E);
      if(absE<len)
      {
        str=str.substring(0, len-absE)+"."+str.substring(len-absE);
      }
      else
      {
        for(int  i=0; i<(absE-len); i++)
          str="0"+str;
        str="0."+str;
      }
      if(neg)
        str="-"+str;
    }
    else
    {
      for(int i=0; i<m_E; i++)
        str=str+"0";
      if(neg)
        str="-"+str;
    }
    //
    return str;
  }
  public Float Add(Float value)
  {
    if(value.Equal(ZERO))
      return new Float(this);
    //
    long e1=m_E;
    long e2=value.m_E;
    long v1=m_Val;
    long v2=value.m_Val;
    // E must be equal in both operators
    while (e1 != e2)
    {
      if(e1 > e2)
      {
        if(Math.abs(v1)<Long.MAX_VALUE/100)
        {
          v1*=10;
          e1--;
        }
        else
        {
          v2/=10;
          e2++;
        }
      }
      else
      if(e1 < e2)
      {
        if(Math.abs(v2)<Long.MAX_VALUE/100)
        {
          v2*=10;
          e2--;
        }
        else
        {
          v1/=10;
          e1++;
        }
      }
    }
    //
    if( (v1>0 && v2>Long.MAX_VALUE-v1) || (v1<0 && v2<Long.MIN_VALUE-v1) )
    {
      v1/=10; e1++;
      v2/=10; e2++;
    }
    //
    if(v1>0 && v2>Long.MAX_VALUE-v1)
      return new Float(ERROR);
    else
    if(v1<0 && v2<Long.MIN_VALUE-v1)
      return new Float(ERROR);
    //
    return new Float(v1+v2, e1);
  }
  public Float Sub(Float value)
  {
    if(value.Equal(ZERO))
      return new Float(m_Val, m_E);
    return Add(new Float(-value.m_Val, value.m_E));
  }
  public Float Mul(long value)
  {
    return Mul(new Float(value, 0));
  }
  public Float Mul(Float value)
  {
    if(value.Equal(ONE))
      return new Float(this);
    if(value.Equal(ZERO) || this.Equal(ZERO))
      return new Float(ZERO);
    // Check overflow and underflow
    do
    {
      if(Math.abs(value.m_Val)>Math.abs(m_Val))
      {
        if(Long.MAX_VALUE/Math.abs(m_Val)<Math.abs(value.m_Val))
        {
          value.m_Val/=10;
          value.m_E++;
        }
        else
          break;
      }
      else
      {
        if(Long.MAX_VALUE/Math.abs(value.m_Val)<Math.abs(m_Val))
        {
          m_Val/=10;
          m_E++;
        }
        else
          break;
      }
    } while(true);
    //
    long e=m_E+value.m_E;
    long v=m_Val*value.m_Val;
    return new Float(v, e);
  }
  public Float Div(long value)
  {
    return Div(new Float(value, 0));
  }
  public Float Div(Float value)
  {
    if(value.Equal(ONE))
      return new Float(this);
    //
    long e1=m_E;
    long e2=value.m_E;
    long v1=m_Val;
    if(v1==0L)
      return new Float(ZERO);
    long v2=value.m_Val;
    if(v2==0L)
      return new Float(ERROR);
    //
    long val=0L;
    while(true)
    {
      val+=(v1/v2);
      v1%=v2;
      if(v1==0L || Math.abs(val)>(Long.MAX_VALUE/10L))
        break;
      if(Math.abs(v1)>(Long.MAX_VALUE/10L))
      {
        v2/=10L;
        e2++;
      }
      else
      {
        v1*=10L;
        e1--;
      }
      val*=10L;
    }
    //
    Float f=new Float(val, e1-e2);
    f.RemoveZero();
    return f;
  }
  public void RemoveZero()
  {
    if(m_Val==0)
      return;
    while ( m_Val%10 == 0 )
    {
     m_Val/=10;
     m_E++;
    }
  }
  public boolean Great(Float x)
  {
    long e1=m_E;
    long e2=x.m_E;
    long v1=m_Val;
    long v2=x.m_Val;
    //
    while (e1 != e2)
  {
    if(e1 > e2)
    {
      if(Math.abs(v1)<Long.MAX_VALUE/100)
      {
        v1*=10;
        e1--;
      }
      else
      {
        v2/=10;
        e2++;
      }
    }
    else
    if(e1 < e2)
    {
      if(Math.abs(v2)<Long.MAX_VALUE/100)
      {
        v2*=10;
        e2--;
      }
      else
      {
        v1/=10;
        e1++;
      }
    }
  }
  //
  return v1>v2;
  }
  public boolean Less(long x)
  {
    return Less(new Float(x, 0));
  }
  public boolean Less(Float x)
  {
    long e1=m_E;
    long e2=x.m_E;
    long v1=m_Val;
    long v2=x.m_Val;
    //
    while (e1 != e2)
  {
    if(e1 > e2)
    {
      if(Math.abs(v1)<Long.MAX_VALUE/100)
      {
        v1*=10;
        e1--;
      }
      else
      {
        v2/=10;
        e2++;
      }
    }
    else
    if(e1 < e2)
    {
      if(Math.abs(v2)<Long.MAX_VALUE/100)
      {
        v2*=10;
        e2--;
      }
      else
      {
        v1/=10;
        e1++;
      }
    }
  }
  //
  return v1<v2;
  }
  public boolean Equal(Float x)
  {
    long e1=m_E;
    long e2=x.m_E;
    long v1=m_Val;
    long v2=x.m_Val;
    //
    while (e1 != e2)
    {
      if(e1 > e2)
      {
        if(Math.abs(v1)<Long.MAX_VALUE/100)
        {
          v1*=10;
          e1--;
        }
        else
        {
          v2/=10;
          e2++;
        }
      }
      else
      if(e1 < e2)
      {
        if(Math.abs(v2)<Long.MAX_VALUE/100)
        {
          v2*=10;
          e2--;
        }
        else
        {
          v1/=10;
          e1++;
        }
      }
    }
    //
    return (v1==v2);
  }
  public Float Neg()
  {
    return new Float(-m_Val, m_E);
  }
  // Math section
  static public Float sin(Float x)
  {
    while( x.Great(PI) )
      x=x.Sub(PImul2);
    while( x.Less(PI.Neg()) )
      x=x.Add(PImul2);
    // x*x*x
    Float m1=x.Mul(x.Mul(x));
    Float q1=m1.Div(6L);
    // x*x*x*x*x
    Float m2=x.Mul(x.Mul(m1));
    Float q2=m2.Div(120L);
    // x*x*x*x*x*x*x
    Float m3=x.Mul(x.Mul(m2));
    Float q3=m3.Div(5040L);
    // x*x*x*x*x*x*x*x*x
    Float m4=x.Mul(x.Mul(m3));
    Float q4=m4.Div(362880L);
    // x*x*x*x*x*x*x*x*x*x*x
    Float m5=x.Mul(x.Mul(m4));
    Float q5=m5.Div(39916800L);
    //
    Float result=x.Sub(q1).Add(q2).Sub(q3).Add(q4).Sub(q5);
    //
    if(result.Less(ONE.Neg()))
      return new Float(-1L);
    if(result.Great(ONE))
      return new Float(1L);
    //
    return result;
  }
  static public Float cos(Float x)
  {
    while( x.Great(PI) )
      x=x.Sub(PImul2);
    while( x.Less(PI.Neg()) )
      x=x.Add(PImul2);
    // x*x
    Float m1=x.Mul(x);
    Float q1=m1.Div(2L);
    // x*x*x*x
    Float m2=m1.Mul(m1);
    Float q2=m2.Div(24L);
    // x*x*x*x*x*x
    Float m3=m1.Mul(m2);
    Float q3=m3.Div(720L);
    // x*x*x*x*x*x*x*x
    Float m4=m2.Mul(m2);
    Float q4=m4.Div(40320L);
    Float result=ONE.Sub(q1).Add(q2).Sub(q3).Add(q4);
    //
    if(result.Less(-1L))
      return new Float(-1L);
    if(result.Great(ONE))
      return new Float(1L);
    //
    return result;
  }
  static public Float sqrt(Float x)
  {
    int sp=0;
    boolean inv=false;
    Float a,b;
    //
    if(x.Less(ZERO) || x.Equal(ZERO))
      return new Float(ZERO);
    if(x.Equal(ONE))
      return new Float(ONE);
    // argument less than 1 : invert it
    if(x.Less(ONE))
    {
      x=ONE.Div(x);
      inv=true;
    }
    // process series of division by 16 until argument is <16
    while(x.Great(new Float(16L)))
    {
      sp++;
      x=x.Div(16L);
    }
    // initial approximation
    a=new Float(2L);
    // Newtonian algorithm
    for(int i=ITNUM; i>0; i--)
    {
      b=x.Div(a);
      a=a.Add(b);
      a=a.Mul(new Float(5L, -1L));
    }
    // multiply result by 4 : as much times as divisions by 16 took place
    while(sp>0)
    {
      sp--;
      a=a.Mul(4L);
    }
    // invert result for inverted argument
    if(inv)
      a=ONE.Div(a);
    return a;
  }
  static public Float tan(Float x)
  {
    Float c=cos(x);
    if(c.Equal(ZERO)) return new Float(ERROR);
    return (sin(x).Div(c));
  }
  static public Float parse(String str, int radix)
  {
    int pos=str.indexOf(".");
    int exp=0;
    if(pos!=-1)
    {
      for(int m=pos+1; m<str.length(); m++)
      {
        if(Character.isDigit(str.charAt(m)))
          exp++;
        else
          break;
      }
      str=str.substring(0, pos)+str.substring(pos+1);
    }
    //
    return new Float(Long.parseLong(str, radix), -exp);
  }
  static public Float acos(Float x)
  {
    return PIdiv2.Sub(asin(x));
  }
  static public Float asin(Float x)
  {
    if( x.Less(ONE.Neg()) || x.Great(ONE) ) return new Float(ERROR);
    if( x.Equal(ONE.Neg()) ) return PIdiv2.Neg();
    if( x.Equal(ONE) ) return PIdiv2;
    return atan(x.Div(sqrt(ONE.Sub(x.Mul(x)))));
  }

  static public Float atan(Float x)
  {
      boolean signChange=false;
      boolean Invert=false;
      int sp=0;
      Float x2, a;
      // check up the sign change
      if(x.Less(ZERO))
      {
          x=x.Neg();
          signChange=true;
      }
      // check up the invertation
      if(x.Great(ONE))
      {
          x=ONE.Div(x);
          Invert=true;
      }
      // process shrinking the domain until x<PI/12
      while(x.Great(PIdiv12))
      {
          sp++;
          a=x.Add(SQRT3);
          a=ONE.Div(a);
          x=x.Mul(SQRT3);
          x=x.Sub(ONE);
          x=x.Mul(a);
      }
      // calculation core
      x2=x.Mul(x);
      a=x2.Add(new Float(14087812, -7));
      a=new Float(55913709, -8).Div(a);
      a=a.Add(new Float(60310579, -8));
      a=a.Sub(x2.Mul(new Float(5160454, -8)));
      a=a.Mul(x);
      // process until sp=0
      while(sp>0)
      {
          a=a.Add(PIdiv6);
          sp--;
      }
      // invertation took place
      if(Invert) a=PIdiv2.Sub(a);
      // sign change took place
      if(signChange) a=a.Neg();
      //
      return a;
  }

  static public Float atan2(Float x, Float y)
  {
      if( y.Equal(ZERO) ) return new Float(ERROR);
      Float f=atan(x.Div(y));
      if(x.m_Val>0 && y.m_Val<0)
        f=f.Add(PI);
      if(x.m_Val<0 && y.m_Val<0)
        f=f.Sub(PI);
      return f;
  }

  // precise
  // x=-35 diff=1.48%
  // x=-30 diff=0.09%
  // x=30 diff=0.09%
  // x=31 diff=0.17%
  // x=32 diff=0.31%
  // x=33 diff=0.54%
  // x=34 diff=0.91%
  // x=35 diff=1.46%
  static public Float exp(Float x)
  {
    if(x.Equal(ZERO))
      return new Float(ONE);
    //
    Float f=new Float(ONE);
    long d=1;
    Float k=null;
    boolean isless=x.Less(ZERO);
    if(isless)
      x=x.Neg();
    k=new Float(x).Div(d);
    //
    for(long i=2; i<50; i++)
    {
      f=f.Add(k);
      k=k.Mul(x).Div(i);
    }
    //
    if(isless)
      return ONE.Div(f);
    else
      return f;
  }

  // precise
  // x=25 diff=0.12%
  // x=30 diff=0.25%
  // x=35 diff=0.44%
  // x=40 diff=0.67%
  static public Float log(Float x)
  {
    if(!x.Great(ZERO))
      return new Float(ERROR);
    //
    Float f=new Float(ZERO);
    //
    Float y1=x.Sub(ONE);
    Float y2=x.Add(ONE);
    Float y=y1.Div(y2);
    //
    Float k=new Float(y);
    y2=k.Mul(y);
    //
    for(long i=1; i<50; i+=2)
    {
      f=f.Add(k.Div(i));
      k=k.Mul(y2);
    }
    return f.Mul(2L);
  }

  // precise y=3.5
  // x=15 diff=0.06%
  // x=20 diff=0.40%
  // x=25 diff=1.31%
  // x=30 diff=2.95%
  // if x negative y must be integer value
  static public Float pow(Float x, Float y)
  {
    if(x.Equal(ZERO))
      return new Float(ZERO);
    if(x.Equal(ONE))
      return new Float(ONE);
    //
    long l=y.toLong();
    boolean integerValue=y.Equal(new Float(l));
    //
    if(integerValue)
    {
      boolean neg=false;
      if(y.Less(0))
        neg=true;
      //
      Float result=new Float(x);
      for(long i=1; i<(neg?-l:l); i++)
        result=result.Mul(x);
      //
      if(neg)
        return ONE.Div(result);
      else
        return result;
    }
    else
    {
      if(x.Great(ZERO))
        return exp(y.Mul(log(x)));
      else
        return new Float(ERROR);
    }
  }
  
}
