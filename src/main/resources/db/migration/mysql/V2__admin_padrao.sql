INSERT INTO `residencia` (`id`, `matricula`, `endereco`, `numero`, `complemento`, `bairro`, `cep`, `cidade`, `uf`, `data_criacao`, `data_atualizacao`)
VALUES (NULL, '8000','Rua Antonio Candido de Oliveira', 5, 'Casa 5','Chácara Tres Marias', '04475492', 'Sorocaba', 'SP', CURRENT_DATE(), CURRENT_DATE());

INSERT INTO `morador` (`id`,`nome`, `email`, `cpf`, `rg`, `senha`, `telefone`, `celular`, `perfil`, `data_atualizacao`, `data_criacao`, `posicao`)
VALUES (NULL, 'Bruno Dias Pastorelli', 'bruno@gmail.com', '35979769056', '342729952', '$2a$10$UftFkustVngaNHOsbNE8h.l/lNsQauk2IUMymJN.cUhQkaJtvnaR.', '1155600310', '11975778998', 'ROLE_ADMIN', CURRENT_DATE(), CURRENT_DATE(), 1);

INSERT INTO `vinculo_residencia` (`id`,`morador_id`, `residencia_id`, `data_vinculo`)
VALUES (NULL, (SELECT `id` FROM `morador` WHERE `cpf`='35979769056'), (SELECT `id` FROM `residencia` WHERE `matricula`='8000'), CURRENT_DATE());


