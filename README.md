# API do DivitiBank

Essa api serve para interagir com os dados do banco de dados
</br> para melhorar o desenvolvimento do app DivitiBank.

## Tecnologias usadas
</br>

- SpringBoot utilizando Java
- MongoDB  
- Swagger para documentação

# Como configurar a API do DivitiBank

### Remotamente

Use o link do [render](https://divitibank-backend.onrender.com/swagger-ui/index.html#/)

### Localmente

Utilize o comando git clone para clonar o repositorio
```gitBash
git clone https://github.com/Diviti-Bank/divitibank-backend.git
```
Instale as dependencias na terminal do projeto
```bash
mvn clean install
```
Execute o projeto springboot na mesma terminal
```bash
mvn spring-boot:run
```
</br> Depois, abra o Swagger pelo localhost utilizando o link http://localhost:8080/swagger-ui.html

Então você já pode testar as funções da api local

# Como utilizar a API

### Para listar as contas por cpf  
```http
GET /contas/buscar/{cpf}
```
Ele vai pegar todas as contas e vai retornar o documento da conta buscada por cpf
</br> exemplo do retorno:

```JSON
{
  "id": "string",
  "nome": "string",
  "sobrenome": "string",
  "getdata_nascimento": "2025-03-21T10:49:42.765Z",
  "cpf": "string",
  "email": "string",
  "senha": "string",
  "saldo": 0,
  "cartoes": [
    {
      "status": "string",
      "credito": 0,
      "tipo_cartao": "string",
      "cor_cartao": "string",
      "aproximacao": true,
      "cvc": 0,
      "nome_cartao": "string",
      "numero_cartao": "string",
      "validade": "string"
    }
  ],
  "extrato": [
    {
      "tipo": "string",
      "origem": "string",
      "quantia": 0,
      "data": "2025-03-21T10:49:42.765Z"
    }
  ]
}
```

### Pegar informações especificas da conta
```http
GET /contas/{cpf}/informacoes
```
Ele pega algumas informações do usuário para poder utilizar em uma tela onde so mostra essas informações do usuário
</br> exemplo de retorno da função:
```JSON
{
  "cpf_usuario": "111.222.333-44",
  "nome_sobrenome": "teste testado",
  "data_nascimento": "1999-01-01T02:00:00.000+00:00"
}
```

### Criar conta corrente
```http
POST /contas/criar
```
Ele vai verificar se há uma outra conta com esse mesmo cpf e caso não tenha, a função cria outra conta
</br> a função vai retornar um json dizendo se deu certo ou não
```JSON
{
  "status": "sucesso",
  "mensagem": "a conta foi inserida com sucesso"
}
```

### Deletar conta corrente
```http
DELETE /contas/{cpf}/excluir
```
Ele vai buscar o cpf do usuário e se ele existir, ele exclui a conta com esse cpf
</br> a função vai retornar um json dizendo se ela conseguiu ou não
```JSON
{
  "status": "erro",
  "mensagem": "já existe uma conta com esse CPF"
}
```

### Fazer transferencias
```http
PUT /contas/transferir/{cpfRemetente}/{cpfDestino}/{dinheiro}/{metodo_pagamento}
```
Ele vai perguntar o cpf do usuário que voce quer transferir e depois perguntar o metodo de pagamento que vai ser um numero de 0 a 2.
O dinheiro escolhido para ser transferido, vai direto para a carteira digital do destinatário.
</br>
cada numero signfica um método de pagamento diferente
</br> O numero 0 é a carteira digital do usuário
</br> O numero 1 é o cartão "blue" do usuário, se ele tiver
</br> O numero 2 é o cartão "black" do usuário, se ele tiver
</br> Se algum dos cartões escolhidos for de crédito, ele vai cobrar na fatura após fazer a transferencia
</br> ele retorna um json caso tenha dado errado ou certo
```JSON
{
  "status": "erro",
  "mensagem": "Saldo insuficiente para a transferência"
}
```

### Gerar comprovante
```http
GET /contas/gerarcomprovante/{cpfRemetente}/{cpfDestino}/{dataTransferencia}/{dinheiroTransferido}
```
ele vai gerar um comprovante mostrando informações de quem recebeu, de quem pagou, a data, a hora e a quantia transferida. 
</br> Vai receber como parâmetro o CPF do remetente, CPF do destinatário e o dinheiro que foi transferido 
</br> ele vai retornar um json das informações de cada usuário
```JSON
{
  "nome_destinatario": "João teste",
  "dinheiro_transferido": "100.0",
  "cpf_destinatario": "111.222.333-54",
  "data_e_hora": "2025-03-25T00:06:33.196+00:00",
  "nome_autor": "Larissa teste",
  "cpf_autor": "333.564.999-45"
}
```

### Criar um cartao
```http
POST /contas/criar/cartao/{cpf}
```
ele vai buscar o cpf do usuário inserido e vai pedir as informações em forma de json para o sistema inserir no cartao, como: crédito, cvc, numero do cartão e etc.
</br> ele vai retornar um json falando se ele conseguiu ou não inserir o cartão
```JSON
{
  "status": "sucesso",
  "mensagem": "o cartão foi inserida com sucesso"
}
```
as informações que serão inseridas são essas:
```JSON
{
  "status": "string",
  "credito": 0,
  "tipo_cartao": "string",
  "cor_cartao": "string",
  "aproximacao": true,
  "cvc": 0,
  "nome_cartao": "string",
  "numero_cartao": "string",
  "validade": "string"
}
```

### Atualizar o status de um cartão
```http
PUT /contas/mudarstatus/{cpf}/{cor}/{status}
```
Essa função vai alterar o status do cartão (se ele é inativo ou ativo). Ele pegar o CPF do usuário, cor do cartao e o status que você deseja mudar (tem que ser 'inativo' ou 'ativo')
</br> a função vai retornar uma mensagem dizendo se deu certo. Caso tenha dado certo, ele vai mostrar o status atual do cartão
```JSON
{
  "mensagem": "o status foi atualizado com sucesso",
  "status atual": "ativo",
  "status": "sucesso"
}
```

### Modificar o credito do cartão
```http
PUT /contas/mudarcredito/{cpf}/{cor}/{novoCredito}
```
Essa função modifica o credito do cartão conforme o sistema desejar. Vai receber como parâmetro o CPF do usuário, a cor do cartão desejado e o crédito novo a ser inserido.
</br> o crédito vai ser sempre adicionado no saldo atual do cartão, então caso o sistema queira descontar o saldo do cartão, é necessário passar o número como negativo.
</br> a função vai retornar uma mensagem dizendo se deu certo ou errado. Caso tenha dado certo, ele vai mostrar o saldo atual do cartão. 
```JSON
{
  "credito atual": 400,
  "mensagem": "o crédito foi atualizado com sucesso",
  "status": "sucesso"
}
```

### Pagar a fatura do cartão
```http
PUT /contas/pagarfatura/{cpf}/{cor}
```
Essa função vai receber o cpf do usuario e a cor do cartão. Ela vai verificar se o usuario tem saldo o suficiente para paga a fatura inteira. Se ele tiver,
ele desconta do usuário e paga a fatura completa
</br> ele vai retornar um json dizendo se deu certo ou não
```JSON
{
  "fatura atual": 0,
  "mensagem": "a fatura foi paga com sucesso",
  "status": "sucesso"
}
```

## licensa do MIT

[MIT](https://choosealicense.com/licenses/mit/)
