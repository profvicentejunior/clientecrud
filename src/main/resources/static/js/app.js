const API = "/api/clientes";

$(document).ready(function () {

    configurarToastr();
    configurarMascaras();
    configurarEventos();

    listarClientes();
});

function configurarToastr() {

    toastr.options = {
        closeButton: true,
        progressBar: true,
        newestOnTop: true,
        positionClass: "toast-top-right",
        timeOut: 4000,
        extendedTimeOut: 1000
    };
}

function configurarMascaras() {

    $("#cpf").mask("000.000.000-00");

    $("#telefone").mask("(00) 00000-0000");
}

function configurarEventos() {

    $("#btnSalvar").click(function () {
        salvarCliente();
    });

    $("#btnAtualizarLista").click(function () {
        listarClientes();
    });

    $("#btnCancelar").click(function () {
        limparFormulario();
    });
}

function listarClientes() {

    $.get(API)

        .done(function (clientes) {

            $("#clientesBody").empty();

            if (!clientes || clientes.length === 0) {

                $("#tabelaClientes")
                    .addClass("d-none");

                $("#semClientes")
                    .removeClass("d-none");

                return;
            }

            $("#tabelaClientes")
                .removeClass("d-none");

            $("#semClientes")
                .addClass("d-none");

            clientes.forEach(function (cliente) {

                $("#clientesBody").append(`

                    <tr id="cliente-${cliente.id}">

                        <td>
                            <span class="badge bg-light text-dark">
                                #${cliente.id}
                            </span>
                        </td>

                        <td>
                            <strong>
                                ${escapeHtml(cliente.nome)}
                            </strong>
                        </td>

                        <td>
                            ${escapeHtml(cliente.email)}
                        </td>

                        <td>
                            ${escapeHtml(cliente.cpf)}
                        </td>

                        <td class="text-center">

                            <button
                                id="editar-${cliente.id}"
                                class="btn btn-outline-warning btn-sm me-1"
                                onclick="editarCliente(${cliente.id})">

                                <i class="bi bi-pencil"></i>

                            </button>

                            <button
                                id="excluir-${cliente.id}"
                                class="btn btn-outline-danger btn-sm"
                                onclick="excluirCliente(${cliente.id})">

                                <i class="bi bi-trash"></i>

                            </button>

                        </td>

                    </tr>

                `);

            });

        })

        .fail(function (xhr) {

            console.error(xhr);

            toastr.error(
                "Não foi possível carregar os clientes.",
                "Erro"
            );

        });
}

function salvarCliente() {

    limparErrosFormulario();

    const id = $("#clienteId").val();

    const cliente = {

        nome: $("#nome").val().trim(),

        email: $("#email").val().trim(),

        cpf: $("#cpf").val().trim(),

        telefone: $("#telefone").val().trim()

    };

    if (!validarFormulario(cliente)) {
        return;
    }

    const metodo = id ? "PUT" : "POST";

    const url = id
        ? `${API}/${id}`
        : API;

    bloquearBotaoSalvar(true);

    $.ajax({

        url: url,

        method: metodo,

        contentType: "application/json",

        data: JSON.stringify(cliente)

    })

        .done(function () {

            if (id) {

                toastr.success(
                    "Cliente atualizado com sucesso.",
                    "Sucesso"
                );

            } else {

                toastr.success(
                    "Cliente cadastrado com sucesso.",
                    "Sucesso"
                );
            }

            limparFormulario();

            listarClientes();

        })

        .fail(function (xhr) {

            tratarErro(xhr);

        })

        .always(function () {

            bloquearBotaoSalvar(false);

        });
}

function editarCliente(id) {

    $.get(`${API}/${id}`)

        .done(function (cliente) {

            $("#clienteId").val(cliente.id);

            $("#nome").val(cliente.nome);

            $("#email").val(cliente.email);

            $("#cpf")
                .val(cliente.cpf)
                .trigger("input");

            $("#telefone")
                .val(cliente.telefone)
                .trigger("input");

            $("#tituloFormulario")
                .text("Editar Cliente");

            $("#btnSalvar")
                .html(`
                    <i class="bi bi-check-circle me-2"></i>
                    Atualizar Cliente
                `);

            $("#btnCancelar")
                .removeClass("d-none");

            $("html, body").animate({
                scrollTop: 0
            }, 300);

        })

        .fail(function (xhr) {

            tratarErro(xhr);

        });
}

function excluirCliente(id) {

    const confirmar = confirm(
        "Deseja realmente excluir este cliente?"
    );

    if (!confirmar) {
        return;
    }

    $.ajax({

        url: `${API}/${id}`,

        method: "DELETE"

    })

        .done(function () {

            toastr.success(
                "Cliente excluído com sucesso.",
                "Sucesso"
            );

            listarClientes();

        })

        .fail(function (xhr) {

            tratarErro(xhr);

        });
}

function validarFormulario(cliente) {

    let valido = true;

    if (!cliente.nome) {

        marcarCampoInvalido(
            "nome",
            "Nome é obrigatório."
        );

        valido = false;

    } else if (cliente.nome.length < 3) {

        marcarCampoInvalido(
            "nome",
            "Nome deve possuir pelo menos 3 caracteres."
        );

        valido = false;
    }

    if (!cliente.email) {

        marcarCampoInvalido(
            "email",
            "E-mail é obrigatório."
        );

        valido = false;

    } else if (!emailValido(cliente.email)) {

        marcarCampoInvalido(
            "email",
            "Informe um e-mail válido."
        );

        valido = false;
    }

    if (!cliente.cpf) {

        marcarCampoInvalido(
            "cpf",
            "CPF é obrigatório."
        );

        valido = false;
    }

    if (!valido) {

        toastr.warning(
            "Verifique os campos obrigatórios.",
            "Atenção"
        );
    }

    return valido;
}

function emailValido(email) {

    const regex =
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    return regex.test(email);
}

function tratarErro(xhr) {

    const resposta = xhr.responseJSON;

    if (!resposta) {

        toastr.error(
            "Erro inesperado ao processar a operação.",
            "Erro"
        );

        return;
    }

    if (resposta.erros) {

        Object.entries(resposta.erros)
            .forEach(function ([campo, mensagem]) {

                marcarCampoInvalido(
                    campo,
                    mensagem
                );

            });

        const mensagens =
            Object.values(resposta.erros);

        toastr.error(
            mensagens.join("<br>"),
            "Dados inválidos"
        );

        return;
    }

    if (resposta.mensagem) {

        toastr.error(
            resposta.mensagem,
            "Erro"
        );

        return;
    }

    toastr.error(
        "Não foi possível concluir a operação.",
        "Erro"
    );
}

function marcarCampoInvalido(campo, mensagem) {

    const input = $(`#${campo}`);

    input.addClass("is-invalid");

    const campoErro =
        $(`#erro${capitalizar(campo)}`);

    if (campoErro.length) {

        campoErro.text(mensagem);
    }
}

function limparErrosFormulario() {

    $(".form-control")
        .removeClass("is-invalid");

    $(".invalid-feedback")
        .text("");
}

function limparFormulario() {

    $("#clienteId").val("");

    $("#nome").val("");

    $("#email").val("");

    $("#cpf").val("");

    $("#telefone").val("");

    $("#tituloFormulario")
        .text("Novo Cliente");

    $("#btnSalvar")
        .html(`
            <i class="bi bi-check-circle me-2"></i>
            Salvar Cliente
        `);

    $("#btnCancelar")
        .addClass("d-none");

    limparErrosFormulario();
}

function bloquearBotaoSalvar(bloquear) {

    const botao =
        $("#btnSalvar");

    if (bloquear) {

        botao
            .prop("disabled", true)
            .html(`
                <span
                    class="spinner-border
                           spinner-border-sm
                           me-2">
                </span>
                Processando...
            `);

    } else {

        const id =
            $("#clienteId").val();

        if (id) {

            botao
                .prop("disabled", false)
                .html(`
                    <i class="bi bi-check-circle me-2"></i>
                    Atualizar Cliente
                `);

        } else {

            botao
                .prop("disabled", false)
                .html(`
                    <i class="bi bi-check-circle me-2"></i>
                    Salvar Cliente
                `);
        }
    }
}

function mostrarMensagem(texto, tipo) {

    if (tipo === "success") {

        toastr.success(
            texto,
            "Sucesso"
        );

    } else if (tipo === "warning") {

        toastr.warning(
            texto,
            "Atenção"
        );

    } else {

        toastr.error(
            texto,
            "Erro"
        );
    }
}

function capitalizar(texto) {

    return texto
        .charAt(0)
        .toUpperCase()
        + texto.slice(1);
}

function escapeHtml(valor) {

    if (valor === null ||
        valor === undefined) {

        return "";
    }

    return $("<div>")
        .text(valor)
        .html();
}