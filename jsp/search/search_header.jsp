<tr style="padding-bottom:10px" height="70" valign=top>
	<td>
		<table width="100%">
		<tr height="70" valign=top>
			<td width="210" height="70">
				<img align=left alt="tradeusedbooks.com" src="/myImages/headerSmall.gif" width="210" height="70">
			</td>
			<td>
				<table>
				<tr>
					<td nowrap align=center>
					<%for (int i=0;i<SearchInterface.SITE.length;i++){
						if (i!=search_page){%>
							<a class="headNotSelect" href="<%=URLInterface.URLSEARCH%>?
							<%=SearchInterface.QUERY+"="+search_urlQuery%>&
							<%=SearchInterface.HEADER+"="+SearchInterface.SITE[i][0]%>">
								<%=SearchInterface.SITE[i][1]%>
							</a>
						<%}else{%>
							<font class="headSelect">
								<%=SearchInterface.SITE[i][1]%>
							</font>
						<%}
					}%>
					</td>
					<td></td>
				</tr>
				<tr>
					<td nowrap>
						<form action="<%=URLInterface.URLSEARCH%>" method=get>
						<input type=text maxLength=256 size=55 value="<%=tools.htmlEncode(search_query)%>" name="<%=SearchInterface.QUERY%>">
						<input type=hidden value="<%=SearchInterface.SITE[search_page][0]%>" name="<%=SearchInterface.HEADER%>">
						<input type=submit value="Search">
						</form>
					</td>
					<td valign=top nowrap>
						<!--<a class="genLinks" href="/advSearch">Advanced Search</a>-->
					</td>
				</tr>
				</table>
			</td>
		</tr>
		</table>
	</td>
</tr>
<tr height="25">
	<td bgcolor=#336699 nowrap>
	<table width="100%" height="100%">
	<tr style="color: #FFFFFF;">
		<td width="50%" nowrap>
		<%=SearchInterface.SITE[search_page][1]%>
		</td>
		<td width="50%" nowrap>
			Showing Results <%=Math.min(search.results_from()+1,search.getTotalResults())%> ~ 
			<%=Math.min(search.results_from()
			+search.results_limit()
			,search.getTotalResults()
			)%> out of <%=search.getTotalResults()%>
		</td>
	</tr>
	</table>
	</td>
</tr>