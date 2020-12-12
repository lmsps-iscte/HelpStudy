As alterações efetuadas na lista das avaliações podem ser sobrescritas caso o utilizador saia da tab e volte a entrar na 
mesma antes que o programa seja fechado, daí o alerta exibido nessa funcionalidade.

Toda a aplicação foi cuidadosamente desenvolvida e testada, sendo que esta versão final não apresenta bugs conhecidos.

Não tratamos todas as exceções de NullPointerException nas ListView da GUI, já que não têm qualquer tipo de efeito na 
interação do utilizador. 

Caso haja algum problema de bloqueio da janela aconselhamos a apagar o ficheiro referente à Tab respetiva. Este evento, 
evento é muito raro mas pode acontecer.

A lista de reminders é ordenada por definição por uma função matemática desenvolvida pelo grupo que se baseia numa
para atribuir uma pontuação eficaz que combina a prioridade e a data do reminder.