CREATE TABLE `residencia` (
  `id` bigint(20) NOT NULL,
  `matricula` varchar(20) NOT NULL,
  `endereco` varchar(100) NOT NULL,
  `numero` varchar(10) NOT NULL,
  `bairro` varchar(100) NOT NULL,
  `cep` varchar(8) NOT NULL,
  `cidade` varchar(100) NOT NULL,
  `uf` varchar(2) NOT NULL,
  `data_criacao` datetime NOT NULL,
  `data_atualizacao` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `morador` (
  `id` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `email` varchar(50) NOT NULL,
  `cpf` varchar(11) NOT NULL,
  `rg` varchar(11) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `telefone` varchar(255) NOT NULL,
  `celular` varchar(255) NOT NULL,
  `perfil` varchar(255) NOT NULL,
  `data_atualizacao` datetime NOT NULL,
  `data_criacao` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `lancamento` (
  `id` bigint(20) NOT NULL,
  `data_pagamento` datetime NOT NULL,
  `mes_referencia` varchar(7),
  `valor` decimal(19,2),
  `data_criacao` datetime NOT NULL, 
  `data_atualizacao` datetime NOT NULL,
  `residencia_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `vinculo_residencia` (
	`id` bigint(20) NOT NULL,
	`morador_id` bigint(20) NOT NULL,
	`residencia_id` bigint(20) NOT NULL,
	`data_vinculo` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for table `residencia`
--
ALTER TABLE `residencia`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `morador`
--
ALTER TABLE `morador`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK4cm1kg523jlopyexjbmi6y54j` (`cpf`);

--
-- Indexes for table `lancamento`
--
ALTER TABLE `lancamento`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK46i4k5vl8wah7feutye9kbpi4` (`residencia_id`);
  
 --
-- Indexes for table `vinculo_residencia`
--
ALTER TABLE `vinculo_residencia`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK46i4k5vl8wah7feutye9kbpi4` (`residencia_id`, `morador_id`);

--
-- AUTO_INCREMENT for table `residencia`
--
ALTER TABLE `residencia`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `morador`
--
ALTER TABLE `morador`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `lancamento`
--
ALTER TABLE `lancamento`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
  
--
-- AUTO_INCREMENT for table `vinculo_residencia`
--
ALTER TABLE `vinculo_residencia`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `lancamento`
--
ALTER TABLE `lancamento`
  ADD CONSTRAINT `FK46i4k5vl8wah7feutye9kbpi4` FOREIGN KEY (`residencia_id`) REFERENCES `residencia` (`id`);
  
--
-- Constraints for table `vinculo_residencia`
--
ALTER TABLE `vinculo_residencia`
  ADD CONSTRAINT `FK46i4k5vl8wah7feutye9kbpi45` FOREIGN KEY (`residencia_id`) REFERENCES `residencia` (`id`);
  
ALTER TABLE `vinculo_residencia`
  ADD CONSTRAINT `FK46i4k5vl8wah7feutye9kbpi56` FOREIGN KEY (`morador_id`) REFERENCES `morador` (`id`);
