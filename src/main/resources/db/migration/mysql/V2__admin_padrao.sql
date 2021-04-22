INSERT INTO `residencia` (`id`, `endereco`, `numero`, `complemento`, `bairro`, `cep`, `cidade`, `uf`, `data_criacao`, `data_atualizacao`)
VALUES (NULL,'Rua Antonio Candido de Oliveira', 5, 'Casa 5','Chácara Tres Marias', '04475492', 'Sorocaba', 'SP', CURRENT_DATE(), CURRENT_DATE());

INSERT INTO `morador` (`id`,`nome`, `email`, `cpf`, `rg`, `senha`, `telefone`, `celular`, `perfil`, `data_atualizacao`, `data_criacao`, `posicao`, `associado`)
VALUES (NULL, 'Bruno Dias Pastorelli', 'bruno@gmail.com', '35979769056', '342729952', '$2a$10$UftFkustVngaNHOsbNE8h.l/lNsQauk2IUMymJN.cUhQkaJtvnaR.', '1155600310', '11975778998', 'ROLE_ADMIN', CURRENT_DATE(), CURRENT_DATE(), 1, 1);

INSERT INTO `vinculo_residencia` (`id`,`morador_id`, `residencia_id`, `data_vinculo`)
VALUES (NULL, (SELECT `id` FROM `morador` WHERE `cpf`='35979769056'), (SELECT `id` FROM `residencia` WHERE `id`=1), CURRENT_DATE());

INSERT INTO `modulo` (`id`, `descricao`, `path_modulo`, `posicao`)
VALUES (NULL, 'Residencia', '/residencia', 1);

INSERT INTO `modulo` (`id`, `descricao`, `path_modulo`, `posicao`)
VALUES (NULL, 'Morador', '/morador', 1);

INSERT INTO `funcionalidade` (`id`, `descricao`, `path_funcionalidade`, `posicao`)
VALUES (NULL, 'Lista Residencias', '/residencias', 1);

INSERT INTO `funcionalidade` (`id`, `descricao`, `path_funcionalidade`, `posicao`)
VALUES (NULL, 'Lista Moradores', '/moradores', 1);

INSERT INTO `acesso` (`id`, `id_usuario`,`id_modulo`, `id_funcionalidade`, `data_cadastro`, `posicao`)
VALUES (NULL, 1, 1, 1, CURRENT_DATE(), 1);