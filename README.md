# API do DivitiBank

essa api serve para interagir com os dados do banco de dados
</br> para melhorar o desenvolvimento do app DivitiBank.

## tecnologias usadas
</br>

- SpringBoot utilizando Java
- MongoDB  
- Swagger para documentação

# Como configurar a API do DivitiBank

### remotamente

use o link do [render](https://divitibank-backend.onrender.com)

### localmente

Utilize o comando git clone para clonar o repositorio
```gitBash
git clone https://github.com/Diviti-Bank/divitibank-backend.git
```
instale as dependencias na terminal do projeto
```bash
mvn clean install
```
execute o projeto springboot na mesma terminal
```bash
mvn spring-boot:run
```
</br> Depois, abra o Swagger pelo localhost utilizando o link http://localhost:8080/swagger-ui.html

então você já pode testar as funções da api local

# como utilizar a API

### para listar as contas por cpf  
```http
GET /contas/buscar/{cpf}
```
ele vai pegar todas as contas e vai retornar o documento da conta buscada por cpf
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

### pegar informações especificas da conta
```http
GET /contas/{cpf}/informacoes
```
ele pega algumas informações do usuario para poder utilizar em uma tela onde so mostra essas informações do usuario
</br> exemplo de retorno da função:
```JSON
{
  "cpf_usuario": "111.222.333-44",
  "nome_sobrenome": "teste testado",
  "data_nascimento": "1999-01-01T02:00:00.000+00:00"
}
```

### criar conta corrente
```http
POST /contas/criar
```
ele vai verificar se há uma outra conta com esse mesmo cpf e caso não tenha, a função cria outra conta
</br> a função vai retornar um json dizendo se deu certo ou não
```JSON
{
  "status": "sucesso",
  "mensagem": "a conta foi inserida com sucesso"
}
```

### deletar conta corrente
```http
DELETE /contas/{cpf}/excluir
```
ele vai buscar o cpf do usuario e se ele existir, ele exclui a conta com esse cpf
</br> a função vai retornar um json dizendo se ela conseguiu ou não
```JSON
{
  "status": "erro",
  "mensagem": "já existe uma conta com esse CPF"
}
```

### fazer transferencias
```http
PUT /contas/transferir/{cpfRemetente}/{cpfDestino}/{dinheiro}/{metodo_pagamento}
```
ele vai perguntar o cpf do usuario que voce quer transferir e depois perguntar o metodo de pagamento que vai ser um numero de 0 a 2
</br> numero 0 é a carteira digital do usuario
</br> numero 1 é o cartão "blue" do usuario, se ele tiver
</br> numero 2 é o cartão "black" do usuario, se ele tiver
</br> ele retorna um json caso tenha dado errado ou certo
```JSON
{
  "status": "erro",
  "mensagem": "Saldo insuficiente para a transferência"
}
```





