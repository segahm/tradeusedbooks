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
						if (i!=0){%>
							<a class="headNotSelect" href="/p/<%=SearchInterface.SITE[i][0]%>">
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
						<input type=text maxLength=256 size=55 value="" name="<%=SearchInterface.QUERY%>">
						<input type=hidden value="<%=SearchInterface.SITE[0][0]%>" name="<%=SearchInterface.HEADER%>">
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
	<td bgcolor=#336699 style="color: #FFFFFF;" nowrap><%=HEADERTITLE%></td>
</tr>