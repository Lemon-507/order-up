# Template execution contract

## Reference

- Absolute path: `C:\Users\shibo\Desktop\JavaFX-doc\Day04(9.10)\XX项目_详细设计说明书_v3.0.docx`
- SHA-256: `b0a36132542947a9fef1875aa82d19309d671567c691728c7451fc31418a44e0`
- Page count: 5, verified from the Word PDF export in `.codex-doc-work/template-render`
- Section count: 1
- Evidence: `.codex-doc-work/template-structure.json`, `.codex-doc-work/template-style-evidence.json`, `.codex-doc-work/template-render/page-1.png` through `page-5.png`

## Page system

- A4 portrait, 8.27 by 11.69 inches
- Margins: left 1.25 inches, right 1.25 inches, top 1.00 inch, bottom 1.00 inch
- Header distance approximately 0.59 inch; footer distance approximately 0.69 inch
- One section, new-page start, no populated header or footer, no first-page variant

## Typography

- Cover role: custom paragraph style `主标题`, centered, bold, 24 pt base, Chinese runs explicitly use SimSun; cover metadata uses 15 pt SimSun
- Heading 1: built-in `Heading 1`, SimHei, 14 pt, bold, black, left aligned
- Heading 2: built-in `Heading 2`, bold, black, justified, approximately 22 pt line spacing
- Body: `Normal`, Times New Roman 12 pt base with Chinese runs rendered using the document's East Asian font settings; 9 pt before and after
- Code: source examples use Cambria 11 pt in the reference. New code blocks may use Consolas 9.5 to 10 pt only if wrapping requires it.
- No header/footer text. No decorative title rule.

## Tables and lists

- Table style name: `Table`
- Tables use a centered caption where present, a stronger top rule and lighter internal horizontal rules, generally no vertical rules.
- Header rows are bold and centered. Short status/value columns are centered; narrative cells are left aligned.
- Existing table roles: main-state transitions, state-machine checklist, substate transitions, method contracts, P0 mapping, design decisions, AI usage, AI review issues.
- Rows may expand and repeat the header across pages. Column widths must follow content rather than equal distribution.

## Components and content flow

1. Cover with project name, document type, members, date, and version.
2. Main state machine: transition table, enum, implementation note, diagram, checklist.
3. Substate machine: explanation, transition table, diagram.
4. Method-level design for major classes.
5. P0 requirement-to-design mapping.
6. Design decision table.
7. AI usage record, two evidence-based code checks, and summary.

## Slot map

- Cover text slots are editable. Member names must be marked pending because the working tree has no member list.
- All instructional yellow/red placeholders are removable and must not survive delivery.
- All empty table rows are editable and may be expanded to fit evidence-backed content.
- The sample state-switching code is replaceable because it is instructional rather than project code.
- State-machine placeholder paragraphs are replaceable with generated diagrams derived from current source behavior.
- AI sections are editable only with evidence from repository history and the current Codex documentation task. Unknown provenance must remain explicitly uncertain.
- Styles, theme, page geometry, numbering definitions, and blank headers/footers are preserve-only unless a layout repair requires a minimal change.

## Package preservation

- Preserve `[Content_Types].xml`, root relationships, styles, theme, font table, settings, footnotes, and core/app/custom properties unless the authoring library necessarily updates document metadata.
- Preserve the single-section geometry and blank header/footer relationship behavior.
- `word/document.xml` and its document relationships are editable for replacing template content and inserting diagrams.
- Baseline package inventory is recorded in `.codex-doc-work/template-structure.json` with path, size, and CRC.

## Fidelity gates

- The output remains A4 portrait with the same margins and black heading system.
- The cover remains visually consistent with the reference.
- The four main chapters remain in the reference order.
- Every instructional highlight and empty placeholder is removed.
- Tables remain readable without clipped text; headers repeat when tables span pages.
- Diagrams remain inside the text area and legible at normal zoom.
- The reference file remains unchanged and retains the recorded SHA-256.
