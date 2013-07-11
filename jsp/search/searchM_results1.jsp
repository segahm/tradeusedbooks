<tr valign=top>
	<td width="125" 
	style="font-size:14px;font-family:Arial" align=center 
	nowrap>
		<%if (result_isbn != null){%>
			<%=result_isbn%><br>
		<%}%>
			<a href="<%=URLInterface.URLBOOK+result_id%>">
		<%if (result_imageURL != null){%>
			<img alt="<%=result_imageTitle%>" 
			border=1 style="border-color:#000000" src="<%=result_imageURL%>" width=39 height=60>
		<%}%>
		<%if (result_imageURL == null){%>
			<img alt="<%=result_imageTitle%>" 
			border=0 src="/myImages/noimage.gif" width="90" height="90">
		<%}%>
			</a>
	</td>
	<td width="400" style="word-wrap:break-word;" 
	align=left>
		<a class="titleLinks" 
		href="<%=URLInterface.URLBOOK+result_id%>"><%=tools.htmlEncode(result_title)%></a>
			<br><font face="Courier">Author:
			</font> <%=tools.htmlEncode(result_author)%>
			<br><font face="Courier">Price:
			</font> &#36;<%=result_price%>
			<%if (result_publisher != null){%>
				<br><font face="Courier">Publisher:
				</font> <%=result_publisher%>
			<%}%>
			<%if (result_numberOfPages != null){%>
				<br><font face="Courier"># of Pages:
				</font> <%=result_numberOfPages%>
			<%}%>
	</td>
</tr>