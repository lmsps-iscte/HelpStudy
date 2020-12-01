
object About {

  //About section of the program
  def about(): String = {
    val ver = "Versão 0.1\n"
    val moto = "O teu melhor melhor amigo na hora do estudo\n"
    val dev = "Desenvolvido por: João Polónio, Luís Pires e Tiago Torres\n"
    val email = "Email: helpstudy@iscte.pt"
    ver + moto + dev + email
  }
}