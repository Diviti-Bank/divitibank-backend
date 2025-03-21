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
instale as dependencias
```bash
npm install
```
</br> Depois de executar o projeto em sua IDE, abra o Swagger pelo localhost utilizando o link http://localhost:8080/swagger-ui.html

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


