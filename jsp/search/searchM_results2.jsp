<tr 
<%if (rowOdd){%>
	class='rowOdd'
<%}else{%>
	class='rowEven'
<%}%> 
valign=top align=left>
<td width="100" noWrap align=middle 
style="font-size:12px;font-family: Times News Roman;">
<%if (result_isbn != null){%>
<%=result_isbn%>
<%}%>
</td>
<td align=left width="200" style="word-wrap: break-word">
<a class='titleLinks' 
href='<%=URLInterface.URLBOOK+result_id%>'>
<%=tools.htmlEncode(result_title)%>
</a></td>
<td noWrap 
style="font-size:12px;font-family: Times News Roman;">by 
<%=result_author%>
</td>
<td noWrap 
style="font-size:12px;font-family: Times News Roman;">$<%=result_price%></td>
</tr>