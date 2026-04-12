{{/*
Common labels
*/}}
{{- define "finance-tracker-service.labels" -}}
app: {{ .Chart.Name }}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "finance-tracker-service.selectorLabels" -}}
app: {{ .Chart.Name }}
{{- end }}
