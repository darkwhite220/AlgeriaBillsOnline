package earth.core.data.util


import org.junit.Test

class SignInUtilTest {
    
    @Test
    fun extractSignInPageDataCorrect() {
//        println(SignInUtil.extractSignInPageData(asset_one))
    }
    
    @Test
    fun extractSignInPageDataFailBadPassword() {
//        SignInUtil.extractSignInPageData(asset_bad_password)
    }
    
    @Test
    fun extractSignInPageDataFailBadUsername() {
//        SignInUtil.extractSignInPageData(asset_bad_username)
    }
    
    companion object {
        private const val asset_one = """
        



<!DOCTYPE HTML PUBLIC "-//W3C//Dtd HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<link  href="css/styles.css" rel="stylesheet" type="text/css">
<link href="css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="script/java.js"></script>
<script type="text/javascript" src="script/java.js"></script>
<html>
    <head>
     
       
      
        
        
        



        
        
        

        

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Consultation facture</title>
    </head>

    <body style="background-image:url(images/images6.JPG);background-repeat:repeat-x;" >
        <form name="fprojetedit" id="fprojetedit" action="projetdetailadd.php" method="post" onSubmit="return EW_checkMyForm(this);">
        <div align="center" >
            <div  class="conteneur" >
                <div class="entete" ></div>
                <div class="trait1">
                    <div align="left">
                        <table  align="left">
                            <tr>
                                <td width="10"></td>
                                <td><a class="lien_menu_entete" href="Consult_Facture.jsp" >Facture</a></td>
                                <td>-</td>
                                <td><a class="lien_menu_entete" href="Contact_Dup.jsp" >Contact</a></td>
                               
                                <td>-</td>
                                 <td><a class="lien_menu_entete" href="./commun/logout.jsp" >Se déconnecter</a></td>
                                 
                              
                            </tr>
                        </table>
                           <div align="right">
                            
                            <table style="margin:10px" align="right">
                                <tr>
                                   

                                   

                                   
                                   <td> <a href="#"  onclick="document.location='/fconsultation/modifypass.jsp'" title="Changer mot de passe"><img style="height:32px;width:32px;border:0" src="images/change-password-icon.gif"></a></td>

                                      <td><a  href="#" onClick="window.location='/fconsultation/commun/logout.jsp';" title="Se déconnecter"><img style="height:32px;width:32px;border:0" src="images/shutdown.gif"></a> </td>
                                </tr>
                            </table>
                            
                            
                            
                           </div>

                    </div>

                </div>
                <div  class="corps" >



                    <TABLE  ID="TBody" cellSpacing=0 cellPadding=5 width=790 border=0 >
                        <TR>
                           
                            <td align="center"><span class=titre_form>Consultation des factures Electricité et Gaz</span></td>
                          <br/>
                        </TR>
                        <br/>
                    </TABLE>
                    <table class="table">
                        <TR>
                            <td align="center" width="200" style="background-image:url(images/bleu_gauche.jpg);background-repeat:no-repeat;height:510px">

                                <img src="images/Display-blue-icon-fact.gif">
                                <br><br><br> <br>


                           
                              <br>
                            
                            <br>


                            

                                <br><br><br><br><br><br><br>

                            </td>
                            <TD vAlign=top noWrap  style="background-image:url(images/fond_droit1.jpg);background-repeat:no-repeat;height:510px;" >
                               
                                <TABLE cellSpacing=0 cellPadding=5  border=0>
                                    <tr>
                                        <TD width="180" noWrap >
                                            <div align="left">
                                                <table>
                                                    <tbody>
                                                        <tr>
                                                            <td><img src=images/SDX.jpg width="51" height="82"></td>

                                                              <td ><span align="center" class="sous_titre_form"> &nbsp; Société Algérienne de<br>&nbsp;l'Elèctrcité et du Gaz<br>&nbsp;Distribution<br>&nbsp;</span></td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </TD>

                                        <td colspan="3">
                                            <div align="left">
                                                <table>
                                                    <tbody>
                                                        <tr>
                                                            <td><img src="images/man2.gif" width="16" height="16"></td>
                                                            <td><span class="sous_titre_form">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Client </span></td>
                                                        </tr>
                                                        <TR>
                                                            <TD noWrap width=1></TD>
                                                            <TD align=left width=150 class=champs_form >Référence:</TD>
                                                            <TD align=left width=560><span class="res_form">172005704674134</span></TD>
                                                        </TR>

                                                        <TR>
                                                            <TD noWrap width=1></TD>
                                                            <TD align=left width=150 class=champs_form >Nom et Prénom:</TD>
                                                            <TD align=left width=560><span class="res_form">MR CHABIRA BARKAHOUM</span></TD>
                                                        </TR>
                                                        
                                                      
                                                        <TR>
                                                            <TD noWrap width=1></TD>
                                                            <TD align=left width=150 class=champs_form >Lieu de <br>consommation:</TD>
                                                            <TD align=left width=560><span class="res_form">CITE 94 L BT 12   LOG N  70          </span></TD>
                                                        </TR>
                                                        
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    <table>
                                        <tr>
                                            <td height="20"></td>
                                        </tr>
                                    </table>
                                     <div style="height:325px; overflow:auto">
                                    <table align="center" cellpadding="2" class="table">
                                       <tr >
                                        <p class="orange" style="font-size:11px;color:darkblue;text-align: left" >&nbsp;&nbsp; Date de dernière mise à jours des données du site est le 2024-01-10
                                         </p>
                                         </tr>
                                       
                                        <thead>
                                            <tr class="tr_entete">
                                                <td width="19%" valign="middle">
                                                    Numéro facture
                                                </td>

                                                <td width="19%" valign="middle">
                                                    Date facture
                                                </td>
                                                <td width="50%" valign="middle">
                                                    Trimestre
                                                </td>
                                                


                                                <td width="1%" valign="middle" align="center" colspan=2 >
                                                    Opération
                                                </td>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            
                                            <tr class=tr_pair>
                                                <td align="center">459231108543</td>
                                                <td align="center">2023-11-29</td>
                                                <td align="center">4ème Trimestre 2023</td>
 </td>
                                                 <td align="center"><a onclick="mywindow('fact.jsp?num_fac=459231108543&mtt_ttc=328.940&filial=SDC','fact_459231108543','top=0,left=0,location=0,scrollbars=1,resizable=1,width='+screen.availWidth+',height='+screen.availHeight+',status=0,toolbar=0,directories=0,copyhistory=0')" href="#"><span style="font-size:small; font-weight:bold"><img src="images/views-icon.gif" width="16" height="16" border="0" title="Consulter la facture"/></span></a></td>

                                            </tr>
                                            
                                        </tbody>
                                       
                                        
                                    </table>
                                  </div>
                                  
                                </TABLE>
                            </TD>
                        </TR>
                        
                       
                    </table>



                </div>
                <div class="pied" ></div>
            </div>
        </div>
        </form>
    </body>
</html>
        """
        private const val asset_bad_password = """
        



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">







<script type="text/javascript" src="script/java.js"></script>





<script>alert('Mot de passe incorrect ');document.location='./contenu_index.jsp';</script>



        """
        private const val asset_bad_username = """



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">







<script type="text/javascript" src="script/java.js"></script>





<script>alert('Format utilisateur incorrect ');document.location='./contenu_index.jsp';</script>
"""
    }
}