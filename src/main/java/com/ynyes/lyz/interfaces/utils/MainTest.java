package com.ynyes.lyz.interfaces.utils;

public class MainTest {
	
//	@Autowired
//	TdOrderService tdOrderService;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			String newStr= XMLEncNA("<ERP>"+
					"<TABLE>"+
        "<RT_HEADER_ID>29</RT_HEADER_ID>"+
        "<LINE_ID>25</LINE_ID>"+
        "<COUPON_TYPE_ID>1</COUPON_TYPE_ID>"+
        "<SKU>SCD200-5L</SKU>"+
        "<QUANTITY>1</QUANTITY>"+
        "<PRICE>0.0</PRICE>"+
        "<ATTRIBUTE1></ATTRIBUTE1>"+
        "<ATTRIBUTE2></ATTRIBUTE2>"+
        "<ATTRIBUTE3></ATTRIBUTE3>"+
        "<ATTRIBUTE4></ATTRIBUTE4>"+
        "<ATTRIBUTE5></ATTRIBUTE5>"+
    "</TABLE>"+
    "<TABLE>"+
    "    <RT_HEADER_ID>29</RT_HEADER_ID>"+
     "   <LINE_ID>26</LINE_ID>"+
        "<COUPON_TYPE_ID>1</COUPON_TYPE_ID>"+
        "<SKU>SCD200-5L</SKU>"+
        "<QUANTITY>1</QUANTITY>"+
        "<PRICE>0.0</PRICE>"+
        "<ATTRIBUTE1></ATTRIBUTE1>"+
        "<ATTRIBUTE2></ATTRIBUTE2>"+
        "<ATTRIBUTE3></ATTRIBUTE3>"+
        "<ATTRIBUTE4></ATTRIBUTE4>"+
        "<ATTRIBUTE5></ATTRIBUTE5>"+
    "</TABLE>"+
    "<TABLE>"+
        "<RT_HEADER_ID>29</RT_HEADER_ID>"+
        "<LINE_ID>27</LINE_ID>"+
        "<COUPON_TYPE_ID>4</COUPON_TYPE_ID>"+
        "<SKU>SCD200-5L</SKU>"+
        "<QUANTITY>1</QUANTITY>"+
        "<PRICE>0.0</PRICE>"+
        "<ATTRIBUTE1></ATTRIBUTE1>"+
        "<ATTRIBUTE2></ATTRIBUTE2>"+
        "<ATTRIBUTE3></ATTRIBUTE3>"+
        "<ATTRIBUTE4></ATTRIBUTE4>"+
        "<ATTRIBUTE5></ATTRIBUTE5>"+
    "</TABLE>"+
"</ERP>");
			System.err.println("test -----:"+newStr);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	public static String XMLEncNA(String s) {
        int ln = s.length();
        for (int i = 0; i < ln; i++) {
            char c = s.charAt(i);
            if (c == '<' || c == '>' || c == '&' || c == '"') {
                StringBuffer b =
                        new StringBuffer(s.substring(0, i));
                switch (c) {
                    case '<': b.append("&lt;"); break;
                    case '>': b.append("&gt;"); break;
                    case '&': b.append("&amp;"); break;
                    case '"': b.append("&quot;"); break;
                }
                i++;
                int next = i;
                while (i < ln) {
                    c = s.charAt(i);
                    if (c == '<' || c == '>' || c == '&' || c == '"') {
                        b.append(s.substring(next, i));
                        switch (c) {
                            case '<': b.append("&lt;"); break;
                            case '>': b.append("&gt;"); break;
                            case '&': b.append("&amp;"); break;
                            case '"': b.append("&quot;"); break;
                        }
                        next = i + 1;
                    }
                    i++;
                }
                if (next < ln) b.append(s.substring(next));
                s = b.toString();
                break;
            } // if c ==
        } // for
        return s;
    }

}
