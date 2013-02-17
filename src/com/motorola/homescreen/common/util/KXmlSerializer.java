package com.motorola.homescreen.common.util;

//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.io.Writer;
//import org.xmlpull.v1.XmlSerializer;

public class KXmlSerializer {//implements XmlSerializer {
/*	private int auto;
	private int depth;
	private String[] elementStack = new String[12];
	private String encoding;
	private boolean[] indent = new boolean[4];
	private int[] nspCounts = new int[4];
	private String[] nspStack = new String[8];
	private boolean pending;
	private boolean unicode;
	private Writer writer;

	private final void check(boolean paramBoolean) throws IOException {
		if (!this.pending) {
			return;
		}
		this.depth = (1 + this.depth);
		this.pending = false;
		if (this.indent.length <= this.depth) {
			boolean[] arrayOfBoolean = new boolean[4 + this.depth];
			System.arraycopy(this.indent, 0, arrayOfBoolean, 0, this.depth);
			this.indent = arrayOfBoolean;
		}
		this.indent[this.depth] = this.indent[(-1 + this.depth)];
		int i = this.nspCounts[(-1 + this.depth)];
		if (i < this.nspCounts[this.depth]) {
			this.writer.write(32);
			this.writer.write("xmlns");
			if (!"".equals(this.nspStack[(i * 2)])) {
				this.writer.write(58);
				this.writer.write(this.nspStack[(i * 2)]);
			}
			while ((!"".equals(getNamespace()))
					|| ("".equals(this.nspStack[(1 + i * 2)]))) {
				this.writer.write("=\"");
				writeEscaped(this.nspStack[(1 + i * 2)], 34);
				this.writer.write(34);
				i++;
				break;
			}
			throw new IllegalStateException(
					"Cannot set default namespace for elements in no namespace");
		}
		if (this.nspCounts.length <= 1 + this.depth) {
			int[] arrayOfInt = new int[8 + this.depth];
			System.arraycopy(this.nspCounts, 0, arrayOfInt, 0, 1 + this.depth);
			this.nspCounts = arrayOfInt;
		}
		this.nspCounts[(1 + this.depth)] = this.nspCounts[this.depth];
		Writer localWriter = this.writer;
		if (paramBoolean) {
		}
		for (String str = " />";; str = ">") {
			localWriter.write(str);
			return;
		}
	}

	private final String getPrefix(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    for (int i = -2 + 2 * this.nspCounts[(1 + this.depth)]; i >= 0; i -= 2) {
      if ((this.nspStack[(i + 1)].equals(paramString)) && ((paramBoolean1) || (!this.nspStack[i].equals(""))))
      {
        String str2 = this.nspStack[i];
        for (int m = i + 2;; m++) {
          if (m < 2 * this.nspCounts[(1 + this.depth)])
          {
            if (this.nspStack[m].equals(str2)) {
              str2 = null;
            } 
          }
          else
          {
            if (str2 == null) {
              break;
            } 
            return str2;
          } 
        } 
      } 
    } 
    if (!paramBoolean2) {
      return null;
    } 
    if ("".equals(paramString))
    {
      str1 = "";
      boolean bool = this.pending;
      this.pending = false;
      setPrefix(str1, paramString);
      this.pending = bool;
      return str1;
    } 
    label187:
    StringBuilder localStringBuilder = new StringBuilder().append("n")
    int j = this.auto;
    this.auto = (j + 1);
    String str1 = j;
    for (int k = -2 + 2 * this.nspCounts[(1 + this.depth)];; k -= 2) {
      if (k >= 0)
      {
        if (str1.equals(this.nspStack[k])) {
          str1 = null;
        } 
      }
      else
      {
        if (str1 == null) {
          break label175
        } 
        break;
      } 
    } 
  }	private final void writeEscaped(String paramString, int paramInt)
    throws IOException
  {
    int i = 0;
    if (i < paramString.length())
    {
      int j = paramString.charAt(i);
      switch (j)
      {
      default: 
        label126:if ((j >= 32) && (j != 64) && ((j < 127) || (this.unicode))) {
          this.writer.write(j);
        } 
        break;
      } 
      for (;;)
      {
        i++;
        break;
        if (paramInt == -1)
        {
          this.writer.write(j);
        }
        else
        {
          this.writer.write("&#" + j + ';');
          continue;
          this.writer.write("&");
          continue;
          this.writer.write(">");
          continue;
          this.writer.write("<");
          continue;
          if (j != paramInt) {
            break label92
          } 
          Writer localWriter = this.writer;
          if (j == 34) {}
          for (String str = """;; str = "'")
          {
            localWriter.write(str);
            break;
          } 
          this.writer.write("&#" + j + ";");
        } 
      } 
    } 
  }	public XmlSerializer attribute(String paramString1, String paramString2, String paramString3)
    throws IOException
  {
    int i = 34;
    if (!this.pending) {
      throw new IllegalStateException("illegal position for attribute");
    } 
    if (paramString1 == null) {
      paramString1 = "";
    } 
    String str;
    if ("".equals(paramString1))
    {
      str = "";
      this.writer.write(32);
      if (!"".equals(str))
      {
        this.writer.write(str);
        this.writer.write(58);
      } 
      this.writer.write(paramString2);
      this.writer.write(61);
      if (paramString3.indexOf(i) != -1) {
        break label144
      } 
    } 
    for (;;)
    {
      this.writer.write(i);
      writeEscaped(paramString3, i);
      this.writer.write(i);
      return this;
      str = getPrefix(paramString1, false, true);
      break;
      label146:
      i = 39
    } 
  }	public void cdsect(String paramString) throws IOException {
		check(false);
		this.writer.write("<![CDATA[");
		this.writer.write(paramString);
		this.writer.write("]]>");
	}

	public void comment(String paramString) throws IOException {
		check(false);
		this.writer.write("<!--");
		this.writer.write(paramString);
		this.writer.write("-->");
	}

	public void docdecl(String paramString) throws IOException {
		this.writer.write("<!DOCTYPE");
		this.writer.write(paramString);
		this.writer.write(">");
	}

	public void endDocument() throws IOException {
		while (this.depth > 0) {
			endTag(this.elementStack[(-3 + 3 * this.depth)],
					this.elementStack[(-1 + 3 * this.depth)]);
		}
		flush();
	}

	public XmlSerializer endTag(String paramString1, String paramString2)
			throws IOException {
		if (!this.pending) {
			this.depth = (-1 + this.depth);
		}
		if (((paramString1 == null) && (this.elementStack[(3 * this.depth)] != null))
				|| ((paramString1 != null) && (!paramString1
						.equals(this.elementStack[(3 * this.depth)])))
				|| (!this.elementStack[(2 + 3 * this.depth)]
						.equals(paramString2))) {
			throw new IllegalArgumentException("</{" + paramString1 + "}"
					+ paramString2 + "> does not match start");
		}
		if (this.pending) {
			check(true);
			this.depth = (-1 + this.depth);
		}
		for (;;) {
			this.nspCounts[(1 + this.depth)] = this.nspCounts[this.depth];
			return this;
			if (this.indent[(1 + this.depth)] != 0) {
				this.writer.write("\r\n");
				for (int i = 0; i < this.depth; i++) {
					this.writer.write("  ");
				}
			}
			this.writer.write("</");
			String str = this.elementStack[(1 + 3 * this.depth)];
			if (!"".equals(str)) {
				this.writer.write(str);
				this.writer.write(58);
			}
			this.writer.write(paramString2);
			this.writer.write(62);
		}
	}

	public void entityRef(String paramString) throws IOException {
		check(false);
		this.writer.write(38);
		this.writer.write(paramString);
		this.writer.write(59);
	}

	public void flush() throws IOException {
		check(false);
		this.writer.flush();
	}

	public int getDepth() {
		if (this.pending) {
			return 1 + this.depth;
		}
		return this.depth;
	}

	public boolean getFeature(String paramString) {
		if ("http://xmlpull.org/v1/doc/features.html#indent-output"
				.equals(paramString)) {
			return this.indent[this.depth];
		}
		return false;
	}

	public String getName() {
		if (getDepth() == 0) {
			return null;
		}
		return this.elementStack[(-1 + 3 * getDepth())];
	}

	public String getNamespace() {
		if (getDepth() == 0) {
			return null;
		}
		return this.elementStack[(-3 + 3 * getDepth())];
	}

	public String getPrefix(String paramString, boolean paramBoolean) {
		try {
			String str = getPrefix(paramString, false, paramBoolean);
			return str;
		} catch (IOException localIOException) {
			throw new RuntimeException(localIOException.toString());
		}
	}

	public Object getProperty(String paramString) {
		throw new RuntimeException("Unsupported property");
	}

	public void ignorableWhitespace(String paramString) throws IOException {
		text(paramString);
	}

	public void processingInstruction(String paramString) throws IOException {
		check(false);
		this.writer.write("<?");
		this.writer.write(paramString);
		this.writer.write("?>");
	}

	public void setFeature(String paramString, boolean paramBoolean) {
		if ("http://xmlpull.org/v1/doc/features.html#indent-output"
				.equals(paramString)) {
			this.indent[this.depth] = paramBoolean;
			return;
		}
		throw new RuntimeException("Unsupported Feature");
	}

	public void setOutput(OutputStream paramOutputStream, String paramString)
			throws IOException {
		if (paramOutputStream == null) {
			throw new IllegalArgumentException();
		}
		if (paramString == null) {
		}
		for (OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(
				paramOutputStream);; localOutputStreamWriter = new OutputStreamWriter(
				paramOutputStream, paramString)) {
			setOutput(localOutputStreamWriter);
			this.encoding = paramString;
			if ((paramString != null)
					&& (paramString.toLowerCase().startsWith("utf"))) {
				this.unicode = true;
			}
			return;
		}
	}

	public void setOutput(Writer paramWriter) {
		this.writer = paramWriter;
		this.nspCounts[0] = 2;
		this.nspCounts[1] = 2;
		this.nspStack[0] = "";
		this.nspStack[1] = "";
		this.nspStack[2] = "xml";
		this.nspStack[3] = "http://www.w3.org/XML/1998/namespace";
		this.pending = false;
		this.auto = 0;
		this.depth = 0;
		this.unicode = false;
	}

	public void setPrefix(String paramString1, String paramString2)
			throws IOException {
		check(false);
		if (paramString1 == null) {
			paramString1 = "";
		}
		if (paramString2 == null) {
			paramString2 = "";
		}
		if (paramString1.equals(getPrefix(paramString2, true, false))) {
			return;
		}
		int[] arrayOfInt = this.nspCounts;
		int i = 1 + this.depth;
		int j = arrayOfInt[i];
		arrayOfInt[i] = (j + 1);
		int k = j << 1;
		if (this.nspStack.length < k + 1) {
			String[] arrayOfString2 = new String[16 + this.nspStack.length];
			System.arraycopy(this.nspStack, 0, arrayOfString2, 0, k);
			this.nspStack = arrayOfString2;
		}
		String[] arrayOfString1 = this.nspStack;
		int m = k + 1;
		arrayOfString1[k] = paramString1;
		this.nspStack[m] = paramString2;
	}

	public void setProperty(String paramString, Object paramObject) {
		throw new RuntimeException("Unsupported Property:" + paramObject);
	}

	public void startDocument(String paramString, Boolean paramBoolean)
    throws IOException
  {
    this.writer.write("<?xml version='1.0' ");
    if (paramString != null)
    {
      this.encoding = paramString;
      if (paramString.toLowerCase().startsWith("utf")) {
        this.unicode = true;
      } 
    } 
    if (this.encoding != null)
    {
      this.writer.write("encoding='");
      this.writer.write(this.encoding);
      this.writer.write("' ");
    } 
    Writer localWriter;
    if (paramBoolean != null)
    {
      this.writer.write("standalone='");
      localWriter = this.writer;
      if (!paramBoolean.booleanValue()) {
        break label131
      } 
    } 
    label136:for (String str = "yes";; str = "no")
    {
      localWriter.write(str);
      this.writer.write("' ");
      this.writer.write("?>");
      return;
    } 
  }	public XmlSerializer startTag(String paramString1, String paramString2)
    throws IOException
  {
    check(false);
    if (this.indent[this.depth] != 0)
    {
      this.writer.write("\r\n");
      for (int n = 0; n < this.depth; n++) {
        this.writer.write("  ");
      } 
    } 
    int i = 3 * this.depth;
    if (this.elementStack.length < i + 3)
    {
      String[] arrayOfString3 = new String[12 + this.elementStack.length];
      System.arraycopy(this.elementStack, 0, arrayOfString3, 0, i);
      this.elementStack = arrayOfString3;
    } 
    String str;
    if (paramString1 == null)
    {
      str = "";
      if (!"".equals(paramString1)) {}
    }
    else
    {
      for (int m = this.nspCounts[this.depth];; m++)
      {
        if (m >= this.nspCounts[(1 + this.depth)]) {
          break label210
        } 
        if (("".equals(this.nspStack[(m * 2)])) && (!"".equals(this.nspStack[(1 + m * 2)])))
        {
          throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
          str = getPrefix(paramString1, true, true);
          break;
        } 
      } 
    } 
    label214:
    String[] arrayOfString1 = this.elementStack
    int j = i + 1;
    arrayOfString1[i] = paramString1;
    String[] arrayOfString2 = this.elementStack;
    int k = j + 1;
    arrayOfString2[j] = str;
    this.elementStack[k] = paramString2;
    this.writer.write(60);
    if (!"".equals(str))
    {
      this.writer.write(str);
      this.writer.write(58);
    } 
    this.writer.write(paramString2);
    this.pending = true;
    return this;
  }	public XmlSerializer text(String paramString) throws IOException {
		check(false);
		this.indent[this.depth] = false;
		writeEscaped(paramString, -1);
		return this;
	}

	public XmlSerializer text(char[] paramArrayOfChar, int paramInt1,
			int paramInt2) throws IOException {
		text(new String(paramArrayOfChar, paramInt1, paramInt2));
		return this;
	}*/
}

/*
 * Location: J:\鎶�湳鏂囨。\瀹夊崜鍥轰欢鐩稿叧\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.homescreen.common.util.KXmlSerializer JD-Core Version: 0.6.2
 */